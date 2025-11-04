package com.example.food_delivery_app.ui.order


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_delivery_app.data.model.Order
import com.example.food_delivery_app.data.model.OrderItem
import com.example.food_delivery_app.data.repository.OrderRepository
import com.example.food_delivery_app.ui.cart.CartDisplay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class OrderViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {
    private val _orders = MutableStateFlow<List<OrderDisplay>>(emptyList())
    val orders: StateFlow<List<OrderDisplay>> = _orders.asStateFlow()

    init {
        observeOrders()
    }

    private fun observeOrders() {
        viewModelScope.launch {
            orderRepository.getAllOrders().collect { list ->
                _orders.value = list.map { order ->
                    order.toDisplay() }
            }
        }
    }

    fun addOrder(cartItems: List<CartDisplay>, total: Double) {
        viewModelScope.launch {
            val orderId = orderRepository.insertOrder(
                Order(
                    totalAmount = total,
                    status = "pending",
                    deliveryAddress = "",
                    customerName = "",
                    customerPhone = ""
                )
            ).toInt()
            val items = cartItems.map { display ->
                OrderItem(
                    orderId = orderId,
                    foodId = display.foodId,
                    quantity = display.quantity,
                    price = display.price
                )
            }
            orderRepository.insertOrderItems(items)
        }
    }
}

private fun Order.toDisplay(): OrderDisplay {
    val formatter = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
    val createdText = formatter.format(java.util.Date(createdAt))
    return OrderDisplay(
        id = id,
        totalAmount = totalAmount,
        status = status,
        createdAtText = createdText
    )
}