package com.example.food_delivery_app.ui.order


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow



class OrderViewModel : ViewModel() {
    private val _orders = MutableStateFlow<List<OrderDisplay>>(emptyList())
    val orders: StateFlow<List<OrderDisplay>> = _orders.asStateFlow()

    init {
        seedMockOrders()
    }

    private fun seedMockOrders() {
        _orders.value = listOf(
            OrderDisplay(id = 201, totalAmount = 25.99, status = "pending", createdAtText = "2025-11-01 09:15"),
            OrderDisplay(id = 202, totalAmount = 47.80, status = "delivered", createdAtText = "2025-10-31 18:40")
        )
    }
}