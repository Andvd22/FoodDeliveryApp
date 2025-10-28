package com.example.food_delivery_app.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_delivery_app.data.model.Food
import com.example.food_delivery_app.databinding.ItemCartBinding
import kotlinx.coroutines.withContext

//UI model mock
data class CartDisplay(
    val imageUrl: String,
    val name: String,
    val price: Double,
    val quantity: Int
)

class CartItemAdapter (
    private val onIncrease: (CartDisplay, Int) -> Unit,
    private val onDecrease: (CartDisplay, Int) -> Unit
): RecyclerView.Adapter<CartItemAdapter.CartViewHolder>(){

    private val items = mutableListOf<CartDisplay>()
    fun submitList(newItem: List<CartDisplay>){
        items.clear()
        items.addAll(newItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CartViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class CartViewHolder(
        private val binding: ItemCartBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(item: CartDisplay) = with(binding){
            textFoodName.text = item.name
            textFoodPrice.text = "$$${"%.2f".format(item.price)}"
            textQuantity.text = item.quantity.toString()

            Glide.with(imageFood.context)
                .load(item.imageUrl)
                .into(imageFood)

            buttonIncrease.setOnClickListener { onIncrease(item, adapterPosition) }
            buttonDecrease.setOnClickListener { onDecrease(item, adapterPosition) }
        }
    }
}