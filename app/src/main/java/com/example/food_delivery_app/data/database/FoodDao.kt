package com.example.food_delivery_app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.food_delivery_app.data.model.Food
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    //getALlFood
    @Query("SELECT * FROM foods")
    fun getAllFoods(): Flow<List<Food>>

    //getFoodInCategory
    @Query("SELECT * FROM foods WHERE category = :category")
    fun getFoodsByCategory(category: String): Flow<List<Food>>

    //getFoodByRatePopular
    @Query("SELECT * FROM foods WHERE isPopular = 1")
    fun getPopularFoods(): Flow<List<Food>>

    //getFoodById
    @Query("Select * FROM foods WHERE id = :foodId")
    fun getFoodById(foodId: Int): Food?

    //addNewFood
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: Food)

    //addNewFoodsInTime
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(foods: List<Food>)

    //updateFood
    @Insert
    suspend fun updateFood(food: Food)

    @Insert
    suspend fun deleteFood(food: Food)
}