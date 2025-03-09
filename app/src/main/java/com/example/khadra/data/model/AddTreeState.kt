package com.example.khadra.data.model

import android.net.Uri

data class AddTreeState(
    val name: String = "",
    val location: String = "",
    val imageUri: Uri? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
