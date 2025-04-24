package com.example.khadra.presentation.viewmodel

data class AddTreeState(
    val name: String = "",
    val type: String = "",
    val status: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val imageUrl: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
