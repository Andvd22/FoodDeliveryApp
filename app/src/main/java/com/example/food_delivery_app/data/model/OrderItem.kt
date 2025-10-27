package com.example.food_delivery_app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val orderId: Int,
    val foodId: Int,
    val quantity: Int,
    val price: Double,
    val specialInstructions: String = ""
)