package com.example.food_delivery_app.data.repository

import com.example.food_delivery_app.data.database.FoodDao
import com.example.food_delivery_app.data.model.Food
import kotlinx.coroutines.flow.Flow


class FoodRepository (
    private val foodDao: FoodDao
) {
    fun getAllFoods(): Flow<List<Food>>{
        return foodDao.getAllFoods()
    }

    fun getFoodsByCategory(category: String): Flow<List<Food>>{
        return foodDao.getFoodsByCategory(category)
    }

    // LẤY MÓN ĂN PHỔ BIẾN
    fun getPopularFoods(): Flow<List<Food>> {
        return foodDao.getPopularFoods()
    }

    // LẤY MÓN ĂN THEO ID
    suspend fun getFoodById(foodId: Int): Food? {
        return foodDao.getFoodById(foodId)
    }

    // THÊM MÓN ĂN MỚI
    suspend fun insertFood(food: Food) {
        foodDao.insertFood(food)
    }

    // THÊM NHIỀU MÓN ĂN CÙNG LÚC
    suspend fun insertFoods(foods: List<Food>) {
        foodDao.insertFoods(foods)
    }

    // CẬP NHẬT MÓN ĂN
    suspend fun updateFood(food: Food) {
        foodDao.updateFood(food)
    }

    // XÓA MÓN ĂN
    suspend fun deleteFood(food: Food) {
        foodDao.deleteFood(food)
    }
}