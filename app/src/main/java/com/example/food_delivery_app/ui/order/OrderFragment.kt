package com.example.food_delivery_app.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.food_delivery_app.R
import com.example.food_delivery_app.databinding.FragmentOrderBinding
import com.example.food_delivery_app.FoodDeliveryApp
import com.example.food_delivery_app.ui.order.OrderViewModelFactory
import kotlinx.coroutines.launch

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null

    private val binding get() = _binding!!

    private val orderViewModel: OrderViewModel by activityViewModels() {
        val app = requireActivity().application as FoodDeliveryApp
        OrderViewModelFactory(app.container.orderRepository)
    }
//    private val viewModel: OrderViewModel by viewModels()
    private lateinit var orderAdapter: OrderAdapter


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
        collectState()
    }

    private fun setupRecyclerView(){
        orderAdapter = OrderAdapter(
            onOrderClick = {}
        )
        binding.recyclerOrders.adapter = orderAdapter
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                orderViewModel.orders.collect { orderList ->
                    orderAdapter.submitList(orderList)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}