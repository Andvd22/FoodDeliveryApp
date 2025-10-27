package com.example.food_delivery_app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val foodId: Int,
    val quantity: Int,
    val addedAt: Long = System.currentTimeMillis(),
    val specialInstructions: String = ""
)