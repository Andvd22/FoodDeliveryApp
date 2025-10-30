package com.example.food_delivery_app.ui.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProfileUi(
    val name: String,
    val phone: String,
    val avatarResId: Int // drawable resource id
)

class ProfileViewModel : ViewModel() {
    private val _user = MutableStateFlow(
        ProfileUi("Nguyễn Văn A", "0123 456 789", android.R.drawable.sym_def_app_icon)
    )
    val user: StateFlow<ProfileUi> = _user.asStateFlow()

    private val _menu = MutableStateFlow<List<ProfileMenuItem>>(emptyList())
    val menu: StateFlow<List<ProfileMenuItem>> = _menu.asStateFlow()

    init {
        _menu.value = listOf(
            ProfileMenuItem(android.R.drawable.sym_def_app_icon, "Địa chỉ giao hàng"),
            ProfileMenuItem(android.R.drawable.sym_def_app_icon, "Phương thức thanh toán"),
            ProfileMenuItem(android.R.drawable.sym_def_app_icon, "Lịch sử đơn hàng"),
            ProfileMenuItem(android.R.drawable.sym_def_app_icon, "Cài đặt"),
            ProfileMenuItem(android.R.drawable.sym_def_app_icon, "Đăng xuất")
        )
    }
}