package com.example.food_delivery_app.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.food_delivery_app.R
import com.example.food_delivery_app.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var menuAdapter: ProfileMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        collectState()
    }


    private fun setupRecyclerView(){
        menuAdapter = ProfileMenuAdapter(
            onItemClick = {}
        )
        binding.recyclerProfileMenu.adapter = menuAdapter
    }


    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.user.collect { profile ->
                        binding.textUserName.text = profile.name
                        binding.textUserPhone.text = profile.phone
                        binding.imageAvatar.setImageResource(profile.avatarResId)
                    }
                }
                launch {
                    viewModel.menu.collect { items ->
                        menuAdapter.submitList(items)
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