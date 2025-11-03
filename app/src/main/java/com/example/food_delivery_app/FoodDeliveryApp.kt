package com.example.food_delivery_app

import android.app.Application
import android.content.Context
import com.example.food_delivery_app.data.database.FoodDatabase
import com.example.food_delivery_app.data.repository.CartRepository
import com.example.food_delivery_app.data.repository.FoodRepository
import com.example.food_delivery_app.data.repository.OrderRepository
import com.example.food_delivery_app.data.repository.SeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FoodDeliveryApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

class AppContainer(private val appContext: Context) {
    private val database: FoodDatabase by lazy { FoodDatabase.getDatabase(appContext) }

    val foodRepository: FoodRepository by lazy { FoodRepository(database.foodDao()) }
    val cartRepository: CartRepository by lazy { CartRepository(database.cartDao()) }
    val orderRepository: OrderRepository by lazy { OrderRepository(database.orderDao(), database.orderItemDao()) }

    init {
        // Seed dữ liệu Food lần đầu nếu bảng trống
        CoroutineScope(Dispatchers.IO).launch {
            val existing = foodRepository.getAllFoods().first()
            if (existing.isEmpty()) {
                foodRepository.insertFoods(SeedData.foods)
            }
        }
    }
}


