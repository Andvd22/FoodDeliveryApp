package com.example.food_delivery_app.ui.menu

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.os.Build
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.food_delivery_app.data.model.FileItem
import com.example.food_delivery_app.data.repository.FileRepository
import com.example.food_delivery_app.databinding.FragmentMenuBinding
import com.example.food_delivery_app.ui.cart.CartViewModel
import kotlinx.coroutines.launch

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MenuViewModel by viewModels {
        MenuViewModelFactory(FileRepository())
    }
    private val cartViewModel: CartViewModel by activityViewModels()

    private lateinit var fileAdapter: FileAdapter

    // SỬA LỖI 2: Xóa biến cờ này khỏi Fragment
    // private var triedSafOnce = false

    // SAF: dùng StartActivityForResult để lấy FLAGS cấp quyền lâu dài
    private val openTree =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
            if (res.resultCode == Activity.RESULT_OK) {
                val data = res.data ?: return@registerForActivityResult
                val uri: Uri = data.data ?: return@registerForActivityResult
                val flags = data.flags and
                        (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                requireContext().contentResolver.takePersistableUriPermission(uri, flags)
                saveTreeUri(uri) // LƯU lại để lần sau dùng luôn

                loadFilesFromTree(uri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        collectFileList() // Thằng này bây giờ là NGUỒN SỰ THẬT DUY NHẤT

        // Nếu đã có quyền "All files access" (Android 11+) → quét toàn bộ file
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
            viewModel.loadAllFiles(requireContext()) { /* no-op */ }
            return
        }

        // Nếu đã có thư mục đã chọn và còn quyền → load luôn
        val saved = getSavedTreeUri()
        if (saved != null && hasPersistedReadPermission(saved)) {
            // Hàm này bây giờ sẽ cập nhật ViewModel,
            // và collectFileList sẽ tự động cập nhật UI
            loadFilesFromTree(saved)
        } else {
            // Thử MediaStore trước; nếu rỗng mới bật picker
            checkAndLoadDownloads()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Quyền đã được cấp, bây giờ hãy tải file
                viewModel.loadDownloads(requireContext(), onResult = { list ->
                    // SỬA LỖI 2: Dùng cờ của ViewModel
                    if (list.isEmpty() && !viewModel.triedSafOnce) {
                        viewModel.triedSafOnce = true // Sét cờ của ViewModel
                        openFolderPicker()
                    }
                })
            } else {
                // Quyền bị từ chối
                Toast.makeText(context, "Cần quyền để tải file", Toast.LENGTH_SHORT).show()
                // SỬA LỖI 2: Dùng cờ của ViewModel
                if (!viewModel.triedSafOnce) {
                    viewModel.triedSafOnce = true // Sét cờ của ViewModel
                    openFolderPicker()
                }
            }
        }

    private fun checkAndLoadDownloads() {
        val permission = android.Manifest.permission.READ_MEDIA_IMAGES
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Đã có quyền, tải file
                viewModel.loadDownloads(requireContext(), onResult = { list ->
                    // SỬA LỖI 2: Dùng cờ của ViewModel
                    if (list.isEmpty() && !viewModel.triedSafOnce) {
                        viewModel.triedSafOnce = true // Sét cờ của ViewModel
                        openFolderPicker()
                    }
                })
            }
            shouldShowRequestPermissionRationale(permission) -> {
                requestPermissionLauncher.launch(permission)
            }
            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }


    private fun setupRecyclerView() {
        fileAdapter = FileAdapter(
            onItemClick = { item ->
                Toast.makeText(
                    context,
                    "Đã chọn file ${item.displayName}",
                    Toast.LENGTH_SHORT
                ).show()
                // TODO: xử lý thêm vào giỏ nếu cần
            }
        )
        binding.recyclerFiles.adapter = fileAdapter
    }

    private fun collectFileList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.files.collect { fileList ->
                        // Đây là nơi duy nhất cập nhật UI
                        fileAdapter.submitList(fileList)
                    }
                }
            }
        }
    }

    private fun openFolderPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
                        Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or
                        Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
            )
        }
        openTree.launch(intent)
    }

    /**
     * SỬA LỖI 1: Hàm này giờ sẽ cập nhật ViewModel,
     * chứ không cập nhật Adapter trực tiếp nữa.
     */
    private fun loadFilesFromTree(treeUri: Uri) {
        val doc = DocumentFile.fromTreeUri(requireContext(), treeUri) ?: return
        val files = doc.listFiles()
            .filter { it.isFile }
            .map { f ->
                FileItem(
                    uri = f.uri,
                    displayName = f.name ?: "(no name)",
                    sizeBytes = f.length(),
                    mimeType = f.type,
                    dateModifiedMillis = f.lastModified()
                )
            }
        if (files.isEmpty()) {
            Toast.makeText(requireContext(), "Thư mục không có file", Toast.LENGTH_SHORT).show()
        }

        // === THAY ĐỔI CỐT LÕI ===
        // fileAdapter.submitList(files) // XÓA DÒNG NÀY
        viewModel.setFilesFromSaf(files) // THÊM DÒNG NÀY
        // === KẾT THÚC THAY ĐỔI ===
    }

    // ====== Lưu/đọc Uri thư mục đã chọn ======
    private val PREFS_NAME = "saf_prefs"
    private val KEY_TREE_URI = "key_tree_uri"

    private fun saveTreeUri(uri: Uri) {
        requireContext().getSharedPreferences(PREFS_NAME, 0)
            .edit().putString(KEY_TREE_URI, uri.toString()).apply()
    }

    private fun getSavedTreeUri(): Uri? {
        val s = requireContext().getSharedPreferences(PREFS_NAME, 0)
            .getString(KEY_TREE_URI, null) ?: return null
        return Uri.parse(s)
    }

    private fun hasPersistedReadPermission(uri: Uri): Boolean {
        val perms = requireContext().contentResolver.persistedUriPermissions
        return perms.any { it.uri == uri && it.isReadPermission }
    }
    // ==========================================

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}