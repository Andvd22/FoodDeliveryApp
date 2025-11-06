package com.example.food_delivery_app.data.repository

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.example.food_delivery_app.data.model.FileItem
import java.io.File
import java.util.Locale

class FileRepository {

    @SuppressLint("InlinedApi")
    suspend fun queryDownloads(context: Context): List<FileItem> {
        val resolver = context.contentResolver
        val results = mutableListOf<FileItem>()

        val collection: Uri
        val projection: Array<String>
        val selection: String?
        val selectionArgs: Array<String>?
        val sortOrder = "${MediaStore.MediaColumns.DATE_MODIFIED} DESC"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            // --- BẮT ĐẦU THAY ĐỔI ---

            // 1. Dùng "Bảng" chung chứa TẤT CẢ các file
            collection = MediaStore.Files.getContentUri("external")

            // 2. Dùng các cột chung của "Bảng" Files
            // (Giống hệt projection của khối "else" cũ)
            projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.MEDIA_TYPE // <-- Thêm cột này để lọc
            )

            // 3. Lọc theo 3 loại media: Ảnh, Video, Audio VÀ kích thước > 0
            selection = """
                (
                    ${MediaStore.Files.FileColumns.MEDIA_TYPE} = ? OR
                    ${MediaStore.Files.FileColumns.MEDIA_TYPE} = ? OR
                    ${MediaStore.Files.FileColumns.MEDIA_TYPE} = ?
                ) AND
                ${MediaStore.Files.FileColumns.SIZE} > 0
            """.trimIndent()

            // 4. Cung cấp các giá trị cho 3 dấu ? ở trên
            selectionArgs = arrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO.toString()
            )

//            // Lọc lại để chỉ lấy file trong thư mục Download
//            selection = """
//                (
//                    ${MediaStore.Images.Media.RELATIVE_PATH} LIKE ? OR
//                    ${MediaStore.Images.Media.RELATIVE_PATH} LIKE ?
//                ) AND
//                ${MediaStore.Images.Media.SIZE} > 0 AND
//                ${MediaStore.Images.Media.IS_PENDING} = 0
//            """.trimIndent()
//
//            selectionArgs = arrayOf("%Download/%", "%Downloads/%")

        } else {
            // CÁCH CŨ (CHO API < 29)
            // Lưu ý: Code này cần quyền READ_EXTERNAL_STORAGE
            // Manifest của bạn không có quyền này, nên nó sẽ fail nếu chạy trên API < 29
            collection = MediaStore.Files.getContentUri("external")

            projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATE_MODIFIED
            )

            selection = """
                (
                    ${MediaStore.Files.FileColumns.DATA} LIKE ? OR
                    ${MediaStore.Files.FileColumns.DATA} LIKE ?
                ) AND ${MediaStore.Files.FileColumns.SIZE} > 0
            """.trimIndent()

            selectionArgs = arrayOf("%Download%", "%Downloads%")
        }

        // Bắt đầu truy vấn
        try {
            resolver.query(collection, projection, selection, selectionArgs, sortOrder)?.use { cursor ->

                val idCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                val nameCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
                val mimeCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)
                val dateCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idCol)
                    val contentUri = ContentUris.withAppendedId(collection, id)
                    val name = cursor.getString(nameCol) ?: "(no name)"
                    val size = cursor.getLong(sizeCol)
                    val mime = cursor.getString(mimeCol)
                    val dateSec = cursor.getLong(dateCol)

                    results += FileItem(
                        uri = contentUri,
                        displayName = name,
                        sizeBytes = size,
                        mimeType = mime,
                        dateModifiedMillis = dateSec * 1000
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("FileRepository", "Lỗi khi truy vấn MediaStore", e)
        }

        Log.d("FileRepository", "Truy vấn (API ${Build.VERSION.SDK_INT}) với collection '$collection' tìm thấy ${results.size} file.")
        return results
    }

    /**
     * Quét toàn bộ bộ nhớ ngoài khi app có quyền MANAGE_EXTERNAL_STORAGE (Android 11+).
     * Trả về danh sách FileItem cho mọi loại tệp (pdf, docx, ảnh, video, audio, ...).
     */
    suspend fun scanAllFiles(context: Context): List<FileItem> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return emptyList()

        val root: File = Environment.getExternalStorageDirectory()
        if (!root.exists() || !root.canRead()) return emptyList()

        val results = ArrayList<FileItem>(1024)
        try {
            // Duyệt đệ quy, bỏ qua các thư mục hệ thống tiềm ẩn lỗi/không cần thiết
            val skipNames = setOf("Android", ".thumbnails", ".cache")

            fun walk(dir: File) {
                val children = dir.listFiles() ?: return
                for (child in children) {
                    if (child.isDirectory) {
                        if (child.name in skipNames) continue
                        walk(child)
                    } else if (child.isFile) {
                        results.add(fileToItem(child))
                    }
                }
            }

            walk(root)
        } catch (e: Exception) {
            Log.e("FileRepository", "Lỗi khi quét tất cả file", e)
        }
        return results
    }

    private fun fileToItem(file: File): FileItem {
        val uri = Uri.fromFile(file)
        val mime = guessMimeType(file)
        val size = runCatching { file.length() }.getOrDefault(0L)
        val lastMod = runCatching { file.lastModified() }.getOrDefault(0L)
        return FileItem(
            uri = uri,
            displayName = file.name,
            sizeBytes = size,
            mimeType = mime,
            dateModifiedMillis = lastMod
        )
    }

    private fun guessMimeType(file: File): String? {
        val name = file.name.lowercase(Locale.getDefault())
        return when {
            name.endsWith(".png") -> "image/png"
            name.endsWith(".jpg") || name.endsWith(".jpeg") -> "image/jpeg"
            name.endsWith(".gif") -> "image/gif"
            name.endsWith(".webp") -> "image/webp"
            name.endsWith(".mp4") -> "video/mp4"
            name.endsWith(".mkv") -> "video/x-matroska"
            name.endsWith(".mp3") -> "audio/mpeg"
            name.endsWith(".wav") -> "audio/wav"
            name.endsWith(".pdf") -> "application/pdf"
            name.endsWith(".doc") -> "application/msword"
            name.endsWith(".docx") -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            name.endsWith(".xls") -> "application/vnd.ms-excel"
            name.endsWith(".xlsx") -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            name.endsWith(".ppt") -> "application/vnd.ms-powerpoint"
            name.endsWith(".pptx") -> "application/vnd.openxmlformats-officedocument.presentationml.presentation"
            name.endsWith(".zip") -> "application/zip"
            name.endsWith(".rar") -> "application/x-rar-compressed"
            else -> null
        }
    }
}