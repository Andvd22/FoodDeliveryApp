package com.example.food_delivery_app.ui.menu

import androidx.lifecycle.ViewModel
import com.example.food_delivery_app.data.model.Food
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MenuViewModel : ViewModel() {
    private val _foods = MutableStateFlow<List<Food>>(emptyList())
    val foods: StateFlow<List<Food>> = _foods.asStateFlow()

    init {
        seedMockFoods()
    }

    private fun seedMockFoods() {
        val mockFoods = listOf(
            Food(
                id = 1,
                name = "Pizza Margherita",
                description = "Cà chua, phô mai mozzarella, basil tươi",
                price = 12.99,
                imageUrl = "https://picsum.photos/200?pizza1",
                category = "Pizza",
                restaurantId = 1,
                isPopular = true,
                preparationTime = 15
            ),
            Food(
                id = 2,
                name = "Burger Bò Phô Mai",
                description = "Bò nướng, phô mai cheddar, sốt đặc biệt",
                price = 9.49,
                imageUrl = "https://picsum.photos/200?burger1",
                category = "Burger",
                restaurantId = 1,
                isPopular = true,
                preparationTime = 12
            ),
            Food(
                id = 3,
                name = "Spaghetti Hải sản",
                description = "Mỳ Ý, hải sản tươi, sốt cà chua",
                price = 11.00,
                imageUrl = "https://picsum.photos/200?spaghetti",
                category = "Pasta",
                restaurantId = 2,
                isPopular = false,
                preparationTime = 13
            )
        )
        _foods.value = mockFoods
    }

    // selectCategory(), addToCart(), loadFoodsFromRepo()...
}

