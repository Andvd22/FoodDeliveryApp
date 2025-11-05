package com.example.food_delivery_app.data.model

import android.net.Uri

data class FileItem(
    val uri: Uri,
    val displayName: String,
    val sizeBytes: Long,
    val mimeType: String?,
    val dateModifiedMillis: Long
)