package com.example.food_delivery_app.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel : ViewModel(){
    private val _items = MutableStateFlow<List<CartDisplay>>(emptyList())
    val items: StateFlow<List<CartDisplay>> = _items.asStateFlow()

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount.asStateFlow()

    init {
        seedMock()
    }

    private fun seedMock() {
        val mock = listOf(
            CartDisplay(
                imageUrl = "https://picsum.photos/200?pizza2",
                name = "Pizza Hải Sản",
                price = 14.49,
                quantity = 1
            ),
            CartDisplay(
                imageUrl = "https://picsum.photos/200?burger2",
                name = "Burger Gà Giòn",
                price = 8.25,
                quantity = 2
            )
        )
        _items.value = mock
        recalcTotal()
    }

    private fun recalcTotal() {
        _totalAmount.value = _items.value.sumOf {  cartItem ->
            cartItem.price * cartItem.quantity
        }
    }

    fun increase(name: String){
        viewModelScope.launch {
            _items.update { currentList ->
                currentList.map { cartItem ->
                    if(cartItem.name == name) cartItem.copy(quantity = cartItem.quantity + 1) else cartItem
                }
            }
            recalcTotal()
        }
    }

    fun decrease(name: String){
        viewModelScope.launch {
            _items.update { currentList ->
                currentList.map { cartItem ->
                    if(cartItem.name == name) cartItem.copy(quantity = (cartItem.quantity - 1).coerceAtLeast(1)) else cartItem
                }
            }
            recalcTotal()
        }
    }

    fun remove(name: String) {
        viewModelScope.launch {
            _items.update { current ->
                current.filterNot { cartItem ->
                    cartItem.name == name } }
            recalcTotal()
        }
    }

    fun clear() {
        viewModelScope.launch {
            _items.value = emptyList()
            recalcTotal()
        }
    }
}