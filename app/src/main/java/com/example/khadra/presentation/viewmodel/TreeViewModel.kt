package com.example.khadra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khadra.data.model.Tree
import com.example.khadra.data.repository.TreeRepository
import com.example.khadra.data.repository.TreeRepositoryImpl
import com.example.khadra.domain.usecase.GetTreesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UIState (
    val isLoading: Boolean = false,
    val trees: List<Tree> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class TreeViewModel @Inject constructor(private val getTreesUseCase: GetTreesUseCase ) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        getTrees()
    }

    private fun getTrees() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            // Simulate network delay
            delay(2000)
            getTreesUseCase().catch {
                _uiState.value = _uiState.value.copy(
                    trees = emptyList(),
                    isLoading = false,
                    error = "Failed to fetch trees"
                )
            }.collect { trees ->
                _uiState.value = _uiState.value.copy(
                    trees = trees,
                    isLoading = false
                )
            }
        }
    }
}
