package com.example.khadra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khadra.domain.usecase.AddTreeUseCase
import com.example.khadra.data.model.Tree
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class AddTreeEvent {
    data class NameChanged(val name: String) : AddTreeEvent()
    data class TypeSelected(val type: String) : AddTreeEvent()
    data class StatusSelected(val status: String) : AddTreeEvent()
    data class CoordinatesChanged(val latitude: Double, val longitude: Double) : AddTreeEvent()
    data class ImageUrlChanged(val url: String) : AddTreeEvent()
    object Submit : AddTreeEvent()
}

@HiltViewModel
class AddTreeViewModel @Inject constructor(
    private val addTreeUseCase: AddTreeUseCase
) : ViewModel() {

    // State for form fields
    private val _state = MutableStateFlow(AddTreeState())
    val state: StateFlow<AddTreeState> = _state.asStateFlow()

    // Events that can be triggered
    fun onEvent(event: AddTreeEvent) {
        when (event) {
            is AddTreeEvent.NameChanged -> {
                _state.value = _state.value.copy(name = event.name)
            }
            is AddTreeEvent.TypeSelected -> {
                _state.value = _state.value.copy(type = event.type)
            }
            is AddTreeEvent.StatusSelected -> {
                _state.value = _state.value.copy(status = event.status)
            }
            is AddTreeEvent.CoordinatesChanged -> {
                _state.value = _state.value.copy(
                    coordinates = Pair(event.latitude, event.longitude)
                )
            }
            is AddTreeEvent.ImageUrlChanged -> {
                _state.value = _state.value.copy(imageUrl = event.url)
            }
            AddTreeEvent.Submit -> {
                submitTree()
            }
        }
    }

    // Function to submit the tree
    private fun submitTree() {
        val state = _state.value

        // Validate fields
        if (state.name.isBlank() || state.type.isBlank() || state.status.isBlank() || state.imageUrl.isBlank()) {
            _state.value = _state.value.copy(error = "All fields are required")
            return
        }

        // Create a new Tree object
        val tree = Tree(
            id = System.currentTimeMillis().toString(), // Generate a unique ID
            name = state.name,
            type = state.type,
            status = state.status,
            coordinates = state.coordinates,
            urlImage = state.imageUrl,
            lastIrrigationAction = java.util.Date(),
            createdAt = java.util.Date(),
            updatedAt = java.util.Date()
        )

        // Use the use case to add the tree
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                addTreeUseCase(tree)
                _state.value = _state.value.copy(isSuccess = true, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }
}

data class AddTreeState(
    val name: String = "",
    val type: String = "",
    val status: String = "Healthy",
    val coordinates: Pair<Double, Double> = Pair(0.0, 0.0),
    val imageUrl: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
