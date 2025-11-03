package com.example.food_delivery_app.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_delivery_app.data.model.Food
import com.example.food_delivery_app.data.repository.CartRepository
import com.example.food_delivery_app.data.repository.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository,
    private val foodRepository: FoodRepository
) : ViewModel(){
    private val _items = MutableStateFlow<List<CartDisplay>>(emptyList())
    val items: StateFlow<List<CartDisplay>> = _items.asStateFlow()

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount.asStateFlow()

    init {
        observeCart()
    }

    private fun observeCart() {
        viewModelScope.launch {
            combine(
                cartRepository.getAllCartItems(),
                foodRepository.getAllFoods()
            ) { cartItems, foods ->
                val foodById = foods.associateBy { it.id }
                cartItems.mapNotNull { cartItem ->
                    val food = foodById[cartItem.foodId] ?: return@mapNotNull null
                    CartDisplay(
                        foodId = cartItem.foodId,
                        imageUrl = food.imageUrl,
                        name = food.name,
                        price = food.price,
                        quantity = cartItem.quantity
                    )
                }
            }.collect { displays ->
                _items.value = displays
                _totalAmount.value = displays.sumOf { it.price * it.quantity }
            }
        }
    }

    fun increase(foodId: Int){
        viewModelScope.launch {
            val existing = cartRepository.getCartItemByFoodId(foodId)
            if (existing != null) {
                cartRepository.updateCartItem(existing.copy(quantity = existing.quantity + 1))
            }
        }
    }

    fun decrease(foodId: Int){
        viewModelScope.launch {
            val existing = cartRepository.getCartItemByFoodId(foodId)
            if (existing != null) {
                val newQty = (existing.quantity - 1).coerceAtLeast(1)
                cartRepository.updateCartItem(existing.copy(quantity = newQty))
            }
        }
    }

    fun remove(foodId: Int) {
        viewModelScope.launch {
            cartRepository.removeCartItemByFoodId(foodId)
        }
    }

    fun clear() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }

    fun addToCart(food: Food) {
        viewModelScope.launch {
            val existing = cartRepository.getCartItemByFoodId(food.id)
            if (existing == null) {
                cartRepository.addToCart(
                    com.example.food_delivery_app.data.model.CartItem(
                        foodId = food.id,
                        quantity = 1
                    )
                )
            } else {
                cartRepository.updateCartItem(existing.copy(quantity = existing.quantity + 1))
            }
        }
    }
}
