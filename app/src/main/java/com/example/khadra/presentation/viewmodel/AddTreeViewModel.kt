package com.example.khadra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khadra.data.model.AddTreeEvent
import com.example.khadra.data.model.AddTreeState
import com.example.khadra.data.model.Tree
import com.example.khadra.data.model.TreeType
import com.example.khadra.domain.usecase.AddTreeUseCase
import com.example.khadra.domain.usecase.GetTreeTypesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class AddTreeViewModel @Inject constructor(
    private val addTreeUseCase: AddTreeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddTreeState())
    val state: StateFlow<AddTreeState> = _state.asStateFlow()

    fun onEvent(event: AddTreeEvent) {
        when (event) {
            is AddTreeEvent.NameChanged -> {
                _state.update { it.copy(name = event.name) }
            }
            is AddTreeEvent.LocationChanged -> {
                _state.update { it.copy(location = event.location) }
            }
            is AddTreeEvent.ImageSelected -> {
                _state.update { it.copy(imageUri = event.uri) }
            }
            AddTreeEvent.Submit -> submitTree()
        }
    }

    private fun submitTree() {
        val currentState = _state.value

        // التحقق من الحقول المطلوبة
        val errors = mutableListOf<String>().apply {
            if (currentState.name.isBlank()) add("يرجى إدخال اسم الشجرة")
            if (currentState.location.isBlank()) add("يرجى إدخال الموقع")
            if (currentState.imageUri == null) add("يرجى إضافة صورة")
        }

        if (errors.isNotEmpty()) {
            _state.update { it.copy(error = errors.joinToString("\n")) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val newTree = Tree(
                    id = UUID.randomUUID().toString(),
                    name = currentState.name,
                    type = " Fruit",
                    status = "Healthy",
                    coordinates = Pair(12.23444, -120.230),
                    urlImage = currentState.imageUri?.toString() ?: "",
                    lastIrrigationAction = Date(),
                    createdAt = Date(),
                    updatedAt = Date()
                )

                addTreeUseCase(newTree)
                _state.update { it.copy(isSuccess = true, isLoading = false) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "خطأ في الإضافة: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }
}