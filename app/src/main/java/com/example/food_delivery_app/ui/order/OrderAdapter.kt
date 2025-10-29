package com.example.food_delivery_app.ui.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.food_delivery_app.databinding.ItemOrderBinding

data class OrderDisplay(
    val id: Int,
    val totalAmount: Double,
    val status: String,
    val createdAtText: String
)

class OrderAdapter(
    private val onOrderClick: (OrderDisplay) -> Unit
): RecyclerView.Adapter<OrderAdapter.OrderViewHolder>(){

    private val items = mutableListOf<OrderDisplay>()

    fun submitList(newItems: List<OrderDisplay>){
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: OrderViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class OrderViewHolder(
        private val binding: ItemOrderBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(item: OrderDisplay) = with(binding){
            textOrderId.text = "Đơn #${item.id}"
            textOrderStatus.text = item.status
            textOrderTotal.text = "Tổng: $${"%.2f".format(item.totalAmount)}"
            textOrderDate.text = item.createdAtText
            root.setOnClickListener { onOrderClick(item) }
        }
    }
}