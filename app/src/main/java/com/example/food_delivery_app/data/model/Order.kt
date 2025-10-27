package com.example.food_delivery_app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val totalAmount: Double,
    val status: String, // "pending", "confirmed", "preparing", "delivering", "delivered", "cancelled"
    val createdAt: Long = System.currentTimeMillis(),
    val deliveryAddress: String,
    val customerName: String,
    val customerPhone: String,
    val deliveryFee: Double = 0.0,
    val discount: Double = 0.0,
    val notes: String = ""
)