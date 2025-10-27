package com.example.food_delivery_app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.food_delivery_app.data.model.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao{
    //getAllOrderByDescTime
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getAllOrders(): Flow<List<Order>>

    //get order byID
    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: Int): Order?

    //getOrderByStatus
    @Query("SELECT * FROM orders WHERE status = :status ORDER BY createdAt DESC")
    fun getOrdersByStatus(status: String): Flow<List<Order>>

    // 4. THÊM ĐƠN HÀNG MỚI
    @Insert
    suspend fun insertOrder(order: Order): Long

    // 5. CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG
    @Update
    suspend fun updateOrder(order: Order)

    // 6. XÓA ĐƠN HÀNG
    @Delete
    suspend fun deleteOrder(order: Order)

    //update order by status
    @Query("UPDATE orders SET status = :newStatus WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Int, newStatus: String)

}