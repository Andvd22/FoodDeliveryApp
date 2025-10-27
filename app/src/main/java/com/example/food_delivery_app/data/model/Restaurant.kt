package com.example.food_delivery_app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class Restaurant(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val imageUrl: String,
    val rating: Double,
    val deliveryTime: Int,
    val deliveryFee: Double,
    val address: String,
    val phone: String,
    val isOpen: Boolean = true
)