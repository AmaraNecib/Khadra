package com.example.khadra.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khadra.data.model.Tree
import com.example.khadra.domain.usecase.GetTreesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTreesUseCase: GetTreesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadTrees()
    }

    private fun loadTrees() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                getTreesUseCase().collect { trees ->
                    _uiState.update { it.copy(
                        trees = trees,
                        isLoading = false
                    ) }
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error loading trees", e)
                _uiState.update { it.copy(
                    error = e.message ?: "Error loading trees",
                    isLoading = false
                ) }
            }
        }
    }
}
