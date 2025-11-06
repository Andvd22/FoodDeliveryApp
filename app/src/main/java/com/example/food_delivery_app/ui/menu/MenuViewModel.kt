package com.example.food_delivery_app.ui.menu

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_delivery_app.data.model.FileItem
import com.example.food_delivery_app.data.model.Food
import com.example.food_delivery_app.data.repository.FileRepository
import com.example.food_delivery_app.data.repository.FoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//class MenuViewModel(
//    private val foodRepository: FoodRepository
//) : ViewModel() {
//    private val _foods = MutableStateFlow<List<Food>>(emptyList())
//    val foods: StateFlow<List<Food>> = _foods.asStateFlow()
//
//    init {
//        observeFoods()
//    }
//
//    private fun observeFoods() {
//        viewModelScope.launch {
//            foodRepository.getAllFoods().collect { list ->
//                _foods.value = list
//            }
//        }
//    }
//}

class MenuViewModel(
    private val fileRepository: FileRepository
) : ViewModel() {

    private val _files = MutableStateFlow<List<FileItem>>(emptyList())
    val files: StateFlow<List<FileItem>> = _files.asStateFlow()

    var triedSafOnce = false
    fun loadDownloads(
        context: Context,
        onResult: (fileList: List<FileItem>) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = fileRepository.queryDownloads(context)
            _files.value = list

            // Gọi callback trên Main thread khi xong
            withContext(Dispatchers.Main) {
                onResult(list) // THÊM DÒNG NÀY
            }
        }
    }

    fun loadAllFiles(
        context: Context,
        onResult: (fileList: List<FileItem>) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = fileRepository.scanAllFiles(context)
            _files.value = list

            withContext(Dispatchers.Main) {
                onResult(list)
            }
        }
    }

    /**
     * SỬA LỖI 1: Thêm hàm này để SAF (Cách 2) có thể
     * cập nhật "nguồn sự thật" (ViewModel).
     */
    fun setFilesFromSaf(newFiles: List<FileItem>) {
        _files.value = newFiles
    }
}

