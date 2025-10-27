package com.example.food_delivery_app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class Food (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val restaurantId: Int,
    val isPopular: Boolean = false,
    val preparationTime: Int = 15
)