package com.example.khadra.presentation.viewmodel

import com.example.khadra.data.model.Tree

data class MainUiState(
    val trees: List<Tree> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
