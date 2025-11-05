package com.example.food_delivery_app.data.repository

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.example.food_delivery_app.data.model.FileItem

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
            // === SỬA LỖI: TRUY VẤN NƠI CHÚNG TA CÓ QUYỀN ===
            // Quyền READ_MEDIA_IMAGES cấp quyền cho MediaStore.Images
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.RELATIVE_PATH // Thêm lại để lọc
            )
            // Bỏ lọc "Download", chỉ lọc file rỗng
            selection = "${MediaStore.Images.Media.SIZE} > 0"
            selectionArgs = null

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
}