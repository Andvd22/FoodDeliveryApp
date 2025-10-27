package com.example.food_delivery_app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.food_delivery_app.data.model.CartItem
import kotlinx.coroutines.flow.Flow


@Dao
interface CartDao {
    //getAllItemInCart
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItem>>

    //getCartItemByFoodId
    @Query("SELECT * FROM cart_items WHERE foodId = :foodId")
    suspend fun getCartItemByFoodId(foodId: Int): CartItem?

    //addNewCartItem
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    //updateCartItem
    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    //del all item in cart
    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    //del cartitem byfoodid
    @Query("DELETE FROM cart_items WHERE foodId = :foodId")
    suspend fun removeCartItemByFoodId(foodId: Int)

    //get count all cartitem
    @Query("SELECT COUNT(*) FROM cart_items")
    suspend fun getCartItemCount(): Int

    //get total price all cartItem
    @Query("SELECT SUM(quantity * (SELECT price FROM foods WHERE id = cart_items.foodId)) FROM cart_items")
    suspend fun getCartTotal(): Double?
}