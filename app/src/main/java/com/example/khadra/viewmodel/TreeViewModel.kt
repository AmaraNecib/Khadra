package com.example.khadra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khadra.model.Tree
import com.example.khadra.repository.TreeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UIState (
    val isLoading: Boolean = false,
    val trees: List<Tree> = emptyList(),
    val error: String? = null
)

class TreeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        getTrees()
    }

    private fun getTrees() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Simulate network delay
                delay(2000)
                val trees = TreeRepository.getTrees()
                _uiState.value = _uiState.value.copy(
                    trees = trees,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}
