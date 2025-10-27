package com.example.food_delivery_app.data.repository

import com.example.food_delivery_app.data.database.CartDao
import com.example.food_delivery_app.data.model.CartItem
import kotlinx.coroutines.flow.Flow

class CartRepository(
    private val cartDao: CartDao
) {
    // LẤY TẤT CẢ ITEMS TRONG GIỎ
    fun getAllCartItems(): Flow<List<CartItem>> {
        return cartDao.getAllCartItems()
    }

    // LẤY CART ITEM THEO FOOD ID
    suspend fun getCartItemByFoodId(foodId: Int): CartItem? {
        return cartDao.getCartItemByFoodId(foodId)
    }

    // THÊM MÓN VÀO GIỎ
    suspend fun addToCart(cartItem: CartItem) {
        cartDao.insertCartItem(cartItem)
    }

    // CẬP NHẬT SỐ LƯỢNG TRONG GIỎ
    suspend fun updateCartItem(cartItem: CartItem) {
        cartDao.updateCartItem(cartItem)
    }

    // XÓA MÓN KHỎI GIỎ
    suspend fun removeFromCart(cartItem: CartItem) {
        cartDao.deleteCartItem(cartItem)
    }

    // XÓA TẤT CẢ GIỎ HÀNG
    suspend fun clearCart() {
        cartDao.clearCart()
    }

    // XÓA MÓN THEO FOOD ID
    suspend fun removeCartItemByFoodId(foodId: Int) {
        cartDao.removeCartItemByFoodId(foodId)
    }

    // ĐẾM SỐ MÓN TRONG GIỎ
    suspend fun getCartItemCount(): Int {
        return cartDao.getCartItemCount()
    }

    // TÍNH TỔNG TIỀN GIỎ HÀNG
    suspend fun getCartTotal(): Double? {
        return cartDao.getCartTotal()
    }

    // THÊM MÓN VÀO GIỎ (LOGIC PHỨC TẠP)
    suspend fun addOrUpdateCartItem(foodId: Int, quantity: Int, specialInstructions: String) {
        val existingItem = cartDao.getCartItemByFoodId(foodId)

        if(existingItem != null){
            val updatedItem = existingItem.copy(
                quantity = existingItem.quantity + quantity,
                specialInstructions = specialInstructions
            )
            updateCartItem(updatedItem)
        }
        else{
            val newItem = CartItem(
                foodId = foodId,
                quantity = quantity,
                specialInstructions = specialInstructions
            )
            addToCart(newItem)
        }
    }

}