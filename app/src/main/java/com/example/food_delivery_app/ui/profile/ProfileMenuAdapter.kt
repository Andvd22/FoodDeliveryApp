package com.example.food_delivery_app.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.food_delivery_app.databinding.ItemProfileMenuBinding

data class ProfileMenuItem(
    val iconResId: Int,
    val title: String
)

class ProfileMenuAdapter(
    private val onItemClick: (ProfileMenuItem) -> Unit
) : RecyclerView.Adapter<ProfileMenuAdapter.MenuViewHolder>(){

    private val items = mutableListOf<ProfileMenuItem>()

    fun submitList(newItems: List<ProfileMenuItem>){
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuViewHolder {
        val binding = ItemProfileMenuBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MenuViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MenuViewHolder(
        private val binding: ItemProfileMenuBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ProfileMenuItem) = with(binding){
            imageIcon.setImageResource(item.iconResId)
            textTitle.text = item.title
            root.setOnClickListener { onItemClick(item) }
        }
    }
}