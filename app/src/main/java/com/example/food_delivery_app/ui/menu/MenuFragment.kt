package com.example.food_delivery_app.ui.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.food_delivery_app.databinding.FragmentMenuBinding
import com.example.food_delivery_app.FoodDeliveryApp
import com.example.food_delivery_app.ui.menu.MenuViewModelFactory
import com.example.food_delivery_app.ui.cart.CartViewModel
import kotlinx.coroutines.launch


class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MenuViewModel by viewModels {
        val app = requireActivity().application as FoodDeliveryApp
        MenuViewModelFactory(app.container.foodRepository)
    }

    private val cartViewModel: CartViewModel by activityViewModels()

    private lateinit var foodAdapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        collectFoodList()
    }

    private fun setupRecyclerView(){
        foodAdapter = FoodAdapter(
            onItemClick = {},
            onAddToCartClick = { food ->
                cartViewModel.addToCart(food)
                Toast.makeText(requireContext(), "Thêm ${food.name} vào giỏ hàng", Toast.LENGTH_SHORT).show()
            }
        )
        binding.recyclerFoods.adapter = foodAdapter
    }

    private fun collectFoodList(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.foods.collect { foodList ->
                        foodAdapter.submitList(foodList)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}