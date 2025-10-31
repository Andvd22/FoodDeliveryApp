package com.example.food_delivery_app.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.food_delivery_app.databinding.FragmentCartBinding
import com.example.food_delivery_app.ui.order.OrderViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartItemAdapter
    private val orderViewModel: OrderViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

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
        collectState()
        binding.buttonCheckout.setOnClickListener {
            lifecycleScope.launch {
                val cartList = cartViewModel.items.value
                val total = cartViewModel.totalAmount.value
                if (cartList.isEmpty()) {
                    // Không cho đặt đơn khi giỏ rỗng
                    Toast.makeText(requireContext(), "Giỏ hàng trống!", Toast.LENGTH_SHORT).show()
                } else {
                    orderViewModel.addOrder(cartList, total)
                    cartViewModel.clear()
                    Toast.makeText(requireContext(), "Đặt hàng thành công!", Toast.LENGTH_SHORT).show()
                    // Optional: chuyển sang tab Đơn hàng
                    // (activity as MainActivity).selectTabOrder() nếu bạn có hàm chuyển tab
                }
            }
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartItemAdapter(
            onIncrease = {item, _ ->
                cartViewModel.increase(item.name)
            },
            onDecrease = { item, _ ->
                cartViewModel.decrease(item.name)
            }
        )
        binding.recyclerCartItems.adapter = cartAdapter
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Collect list items
                launch {
                    cartViewModel.items.collect { list ->
                        cartAdapter.submitList(list)
                    }
                }

                // Collect total amount
                launch {
                    cartViewModel.totalAmount.collect { total ->
                        binding.textTotalAmount.text = "Tổng: $${"%.2f".format(total)}"
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