package com.example.food_delivery_app.ui.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.food_delivery_app.data.model.Food
import com.example.food_delivery_app.databinding.ItemFoodBinding

class FoodAdapter (
    private val onItemClick: (Food) -> Unit,
    private val onAddToCartClick: (Food) -> Unit
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>(){

    private val items = mutableListOf<Food>()
    fun submitList(newItem: List<Food>) {
        items.clear()
        items.addAll(newItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FoodViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class FoodViewHolder(
        private val binding: ItemFoodBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Food) = with(binding){
            textFoodName.text = item.name
            textFoodPrice.text = "$${"%.2f".format(item.price)}"
            textFoodDescription.text = item.description

            Glide.with(imageFood.context)
                .load(item.imageUrl)
                .into(imageFood)

            root.setOnClickListener { onItemClick(item) }
            buttonAddToCart.setOnClickListener { onAddToCartClick(item) }
        }
    }
}