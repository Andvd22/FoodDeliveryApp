package com.example.food_delivery_app

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigation: BottomNavigationView

    private val prefsName = "all_files_access_prefs"
    private val keySnoozeUntil = "snooze_until_ts"
//    private val snoozeDurationMs = 10L * 60L * 60L * 1000L // 10 giờ

    private val snoozeDurationMs = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()
        maybeAskForAllFilesAccess()
    }

    private fun setupBottomNavigation(){
        bottomNavigation = findViewById(R.id.bottom_navigation)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigation.setupWithNavController(navController)
    }

    private fun maybeAskForAllFilesAccess() {
        if (!shouldShowAllFilesDialog()) return
        showAllFilesAccessDialog()
    }

    private fun shouldShowAllFilesDialog(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return false
        if (Environment.isExternalStorageManager()) return false
        val now = System.currentTimeMillis()
        val snoozeUntil = getSharedPreferences(prefsName, 0).getLong(keySnoozeUntil, 0L)
        return now >= snoozeUntil
    }

    private fun showAllFilesAccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Cấp quyền truy cập tất cả file")
            .setMessage(
                "Ứng dụng cần quyền truy cập tất cả tệp (PDF, Word, ảnh, video, audio, ...) để hiển thị đầy đủ. " +
                "Bạn có muốn mở cài đặt để tự cấp quyền không?"
            )
            .setPositiveButton("Đồng ý") { _, _ ->
                openAllFilesAccessSettings()
            }
            .setNegativeButton("Để sau") { _, _ ->
                snoozeAllFilesDialog()
            }
            .setCancelable(false)
            .show()
    }

    private fun snoozeAllFilesDialog() {
        val until = System.currentTimeMillis() + snoozeDurationMs
        getSharedPreferences(prefsName, 0)
            .edit()
            .putLong(keySnoozeUntil, until)
            .apply()
    }

    private fun openAllFilesAccessSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    data = Uri.parse("package:" + packageName)
                }
                startActivity(intent)
            } catch (_: ActivityNotFoundException) {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    startActivity(intent)
                } catch (_: Exception) {
                    // Không mở được trang cài đặt, bỏ qua yên lặng
                }
            }
        }
    }
}