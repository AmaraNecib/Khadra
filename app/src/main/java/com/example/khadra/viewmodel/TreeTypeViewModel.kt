package com.example.khadra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khadra.model.TreeType
import com.example.khadra.repository.TreeTypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TreeTypeState(
    val treeTypes: List<TreeType> = emptyList()
)

class TreeTypeViewModel : ViewModel() {
    private val _treeTypesState = MutableStateFlow<TreeTypeState>(TreeTypeState())
    val treeTypesState: StateFlow<TreeTypeState> = _treeTypesState.asStateFlow()


    init {
        getTreeTypes()
    }

    private fun getTreeTypes() {
        viewModelScope.launch {
            
            _treeTypesState.value = _treeTypesState.value.copy(
                treeTypes = TreeTypeRepository.getTreeTypes()
                )
        }
    }
}
