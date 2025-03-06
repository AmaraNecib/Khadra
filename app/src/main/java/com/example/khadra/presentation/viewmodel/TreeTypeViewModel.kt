package com.example.khadra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khadra.data.model.TreeType
import com.example.khadra.data.repository.TreeTypeRepositoryImpl
import com.example.khadra.domain.usecase.GetTreeTypesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TreeTypeState(
    val treeTypes: List<TreeType> = emptyList()
)


@HiltViewModel
class TreeTypeViewModel @Inject constructor(private val getTreeTypesUseCase: GetTreeTypesUseCase ) : ViewModel() {
    private val _treeTypesState = MutableStateFlow<TreeTypeState>(TreeTypeState())
    val treeTypesState: StateFlow<TreeTypeState> = _treeTypesState.asStateFlow()


    init {
        getTreeTypes()
    }

    private fun getTreeTypes() {
        viewModelScope.launch {

            getTreeTypesUseCase().collect{ treeTypes ->

                _treeTypesState.value = _treeTypesState.value.copy(
                    treeTypes = treeTypes
                )

            }
        }
    }
}
