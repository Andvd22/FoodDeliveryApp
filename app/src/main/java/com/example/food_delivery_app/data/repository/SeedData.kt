package com.example.food_delivery_app.data.repository

import com.example.food_delivery_app.data.model.Food

object SeedData {
    val foods: List<Food> = listOf(
        Food(
            name = "Pizza Margherita",
            description = "Cà chua, phô mai mozzarella, basil tươi",
            price = 12.99,
            imageUrl = "https://picsum.photos/200?pizza1",
            category = "Pizza",
            restaurantId = 1,
            isPopular = true,
            preparationTime = 15
        ),
        Food(
            name = "Burger Bò Phô Mai",
            description = "Bò nướng, phô mai cheddar, sốt đặc biệt",
            price = 9.49,
            imageUrl = "https://picsum.photos/200?burger1",
            category = "Burger",
            restaurantId = 1,
            isPopular = true,
            preparationTime = 12
        ),
        Food(
            name = "Spaghetti Hải sản",
            description = "Mỳ Ý, hải sản tươi, sốt cà chua",
            price = 11.00,
            imageUrl = "https://picsum.photos/200?spaghetti",
            category = "Pasta",
            restaurantId = 2,
            isPopular = false,
            preparationTime = 13
        )
    )
}