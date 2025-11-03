package com.example.food_delivery_app.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.food_delivery_app.data.repository.CartRepository
import com.example.food_delivery_app.data.repository.FoodRepository

class CartViewModelFactory(
    private val cartRepository: CartRepository,
    private val foodRepository: FoodRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(cartRepository, foodRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}