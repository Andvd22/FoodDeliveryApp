package com.example.food_delivery_app.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_delivery_app.data.model.Food
import com.example.food_delivery_app.data.repository.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MenuViewModel(
    private val foodRepository: FoodRepository
) : ViewModel() {
    private val _foods = MutableStateFlow<List<Food>>(emptyList())
    val foods: StateFlow<List<Food>> = _foods.asStateFlow()

    init {
        observeFoods()
    }

    private fun observeFoods() {
        viewModelScope.launch {
            foodRepository.getAllFoods().collect { list ->
                _foods.value = list
            }
        }
    }
}

