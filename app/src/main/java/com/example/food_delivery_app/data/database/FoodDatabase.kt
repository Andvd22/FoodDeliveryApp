package com.example.food_delivery_app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.food_delivery_app.data.database.DAO.CartDao
import com.example.food_delivery_app.data.database.DAO.FoodDao
import com.example.food_delivery_app.data.database.DAO.OrderDao
import com.example.food_delivery_app.data.database.DAO.OrderItemDao
import com.example.food_delivery_app.data.model.CartItem
import com.example.food_delivery_app.data.model.Food
import com.example.food_delivery_app.data.model.Order
import com.example.food_delivery_app.data.model.OrderItem
import com.example.food_delivery_app.data.model.Restaurant

@Database(
    entities = [
        Food::class,
        Restaurant::class,
        CartItem::class,
        Order::class,
        OrderItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao

    //Singleton pattern
    companion object {
        @Volatile
        private var INSTANCE: FoodDatabase? = null

        fun getDatabase(context: Context): FoodDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodDatabase::class.java,
                    "food_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}