package com.example.food_delivery_app.data.repository

import com.example.food_delivery_app.data.database.OrderDao
import com.example.food_delivery_app.data.database.OrderItemDao
import com.example.food_delivery_app.data.model.Order
import com.example.food_delivery_app.data.model.OrderItem
import kotlinx.coroutines.flow.Flow

class OrderRepository (
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao
) {

    // LẤY TẤT CẢ ĐƠN HÀNG
    fun getAllOrders(): Flow<List<Order>> {
        return orderDao.getAllOrders()
    }

    // LẤY ĐƠN HÀNG THEO ID
    suspend fun getOrderById(orderId: Int): Order? {
        return orderDao.getOrderById(orderId)
    }

    // LẤY ĐƠN HÀNG THEO TRẠNG THÁI
    fun getOrdersByStatus(status: String): Flow<List<Order>> {
        return orderDao.getOrdersByStatus(status)
    }

    // THÊM ĐƠN HÀNG MỚI
    suspend fun insertOrder(order: Order): Long {
        return orderDao.insertOrder(order)
    }

    // CẬP NHẬT ĐƠN HÀNG
    suspend fun updateOrder(order: Order) {
        orderDao.updateOrder(order)
    }

    // XÓA ĐƠN HÀNG
    suspend fun deleteOrder(order: Order) {
        orderDao.deleteOrder(order)
    }

    // CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG
    suspend fun updateOrderStatus(orderId: Int, status: String) {
        orderDao.updateOrderStatus(orderId, status)
    }

    // LẤY ORDER ITEMS CỦA MỘT ĐƠN HÀNG
    fun getOrderItemsByOrderId(orderId: Int): Flow<List<OrderItem>> {
        return orderItemDao.getOrderItemsByOrderId(orderId)
    }

    // THÊM ORDER ITEM
    suspend fun insertOrderItem(orderItem: OrderItem) {
        orderItemDao.insertOrderItem(orderItem)
    }

    // THÊM NHIỀU ORDER ITEMS CÙNG LÚC
    suspend fun insertOrderItems(orderItems: List<OrderItem>) {
        orderItemDao.insertOrderItems(orderItems)
    }

    // XÓA TẤT CẢ ORDER ITEMS CỦA MỘT ĐƠN HÀNG
    suspend fun deleteOrderItemsByOrderId(orderId: Int) {
        orderItemDao.deleteOrderItemsByOrderId(orderId)
    }
}