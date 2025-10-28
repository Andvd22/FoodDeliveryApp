package com.example.food_delivery_app.ui.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.food_delivery_app.R
import com.example.food_delivery_app.data.model.Food
import com.example.food_delivery_app.databinding.FragmentMenuBinding


class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

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
        loadMockData()
    }

    private fun setupRecyclerView(){
        foodAdapter = FoodAdapter(
            onItemClick = {},
            onAddToCartClick = {}
        )
        binding.recyclerFoods.adapter = foodAdapter
    }

    private fun loadMockData(){
        val mockFoods = listOf(
            Food(
                id = 0,
                name = "Pizza Margherita",
                description = "Cà chua, phô mai mozzarella, basil tươi",
                price = 12.99,
                imageUrl = "https://picsum.photos/200?pizza1",
                category = "Pizza",
                restaurantId = 1,
                isPopular = true,
                preparationTime = 15
            ),
            Food(
                id = 0,
                name = "Burger Bò Phô Mai",
                description = "Bò nướng, phô mai cheddar, sốt đặc biệt",
                price = 9.49,
                imageUrl = "https://picsum.photos/200?burger1",
                category = "Burger",
                restaurantId = 1,
                isPopular = true,
                preparationTime = 12
            )
        )

        foodAdapter.submitList(mockFoods)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}