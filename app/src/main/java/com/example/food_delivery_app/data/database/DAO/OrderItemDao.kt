package com.example.food_delivery_app.data.database.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.food_delivery_app.data.model.OrderItem
import kotlinx.coroutines.flow.Flow


@Dao
interface OrderItemDao {
    //get all order items from order id
    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun getOrderItemsByOrderId(orderId: Int): Flow<List<OrderItem>>

    // 2. LẤY ORDER ITEM THEO ID
    @Query("SELECT * FROM order_items WHERE id = :orderItemId")
    suspend fun getOrderItemById(orderItemId: Int): OrderItem?

    // 3. THÊM ORDER ITEM MỚI
    @Insert
    suspend fun insertOrderItem(orderItem: OrderItem)

    // 4. THÊM NHIỀU ORDER ITEMS CÙNG LÚC
    @Insert
    suspend fun insertOrderItems(orderItems: List<OrderItem>)

    // 5. CẬP NHẬT ORDER ITEM
    @Update
    suspend fun updateOrderItem(orderItem: OrderItem)

    // 6. XÓA ORDER ITEM
    @Delete
    suspend fun deleteOrderItem(orderItem: OrderItem)

    // 7. XÓA TẤT CẢ ORDER ITEMS CỦA MỘT ĐƠN HÀNG
    @Query("DELETE FROM order_items WHERE orderId = :orderId")
    suspend fun deleteOrderItemsByOrderId(orderId: Int)
}