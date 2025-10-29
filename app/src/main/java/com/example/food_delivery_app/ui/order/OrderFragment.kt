package com.example.food_delivery_app.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.food_delivery_app.R
import com.example.food_delivery_app.databinding.FragmentOrderBinding

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null

    private val binding get() = _binding!!

    private lateinit var orderAdapter: OrderAdapter

    private var localOrders = mutableListOf<OrderDisplay>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadMockData()
    }

    private fun setupRecyclerView(){
        orderAdapter = OrderAdapter(
            onOrderClick = {}
        )
        binding.recyclerOrders.adapter = orderAdapter
    }

    private fun loadMockData(){
        localOrders = mutableListOf(
            OrderDisplay(id = 101, totalAmount = 30.99, status = "delivered", createdAtText = "2025-10-28 19:20"),
            OrderDisplay(id = 102, totalAmount = 15.49, status = "preparing", createdAtText = "2025-10-29 09:10"),
            OrderDisplay(id = 103, totalAmount = 45.20, status = "pending", createdAtText = "2025-10-29 10:40")
        )
        orderAdapter.submitList(localOrders.toList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}