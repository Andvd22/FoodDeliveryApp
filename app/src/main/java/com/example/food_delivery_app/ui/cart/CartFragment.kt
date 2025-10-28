package com.example.food_delivery_app.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.food_delivery_app.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartItemAdapter
    private var localItems: MutableList<CartDisplay> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadMockData()
        updateTotal()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartItemAdapter(
            onIncrease = { item, _ ->
                val updated = item.copy(quantity = item.quantity + 1)
                replaceItem(updated)
                updateTotal()
            },
            onDecrease = { item, _ ->
                val newQty = (item.quantity - 1).coerceAtLeast(1)
                val updated = item.copy(quantity = newQty)
                replaceItem(updated)
                updateTotal()
            }
        )
        binding.recyclerCartItems.adapter = cartAdapter
    }

    private fun loadMockData() {
        localItems = mutableListOf(
            CartDisplay(
                imageUrl = "https://picsum.photos/200?pizza2",
                name = "Pizza Hải Sản",
                price = 14.49,
                quantity = 1
            ),
            CartDisplay(
                imageUrl = "https://picsum.photos/200?burger2",
                name = "Burger Gà Giòn",
                price = 8.25,
                quantity = 2
            )
        )
        cartAdapter.submitList(localItems.toList())
    }

    private fun replaceItem(updated: CartDisplay) {
        val idx = localItems.indexOfFirst { it.name == updated.name }
        if (idx != -1) {
            localItems[idx] = updated
            cartAdapter.submitList(localItems.toList())
        }
    }

    private fun updateTotal() {
        val total = localItems.sumOf { it.price * it.quantity }
        binding.textTotalAmount.text = "Tổng: $${"%.2f".format(total)}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}