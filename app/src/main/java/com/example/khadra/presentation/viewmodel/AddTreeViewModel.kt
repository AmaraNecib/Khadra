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

@HiltViewModel
class AddTreeViewModel @Inject constructor(
    private val addTreeUseCase: AddTreeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddTreeState())
    val state: StateFlow<AddTreeState> = _state.asStateFlow()

    sealed class AddTreeEvent {
        data class NameChanged(val name: String) : AddTreeEvent()
        data class TypeSelected(val type: String) : AddTreeEvent()
        data class StatusSelected(val status: String) : AddTreeEvent()
        data class CoordinatesChanged(val latitude: Double, val longitude: Double) : AddTreeEvent()
        data class ImageUriChanged(val uri: String) : AddTreeEvent() // New event
        object Submit : AddTreeEvent()
    }

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
                _state.value = _state.value.copy(coordinates = Pair(event.latitude, event.longitude))
            }
            is AddTreeEvent.ImageUriChanged -> {
                _state.value = _state.value.copy(imageUri = event.uri) // Update image URI
            }
            is AddTreeEvent.Submit -> {
                submitTree()
            }
        }
    }

    private fun submitTree() {
        val state = _state.value

        if (state.name.isBlank() || state.type.isBlank() || state.status.isBlank()) {
            _state.value = state.copy(error = "Please fill all fields")
            return
        }

        val tree = Tree(
            id = System.currentTimeMillis().toString(), // Generate a unique ID
            name = state.name,
            type = state.type,
            status = state.status,
            coordinates = state.coordinates,
            urlImage = state.imageUri, // Use the selected image URI
            lastIrrigationAction = java.util.Date(),
            createdAt = java.util.Date(),
            updatedAt = java.util.Date()
        )

        viewModelScope.launch {
            _state.value = state.copy(isLoading = true, error = null)
            try {
                addTreeUseCase(tree)
                _state.value = state.copy(isSuccess = true, isLoading = false)
            } catch (e: Exception) {
                _state.value = state.copy(error = e.message ?: "Failed to add tree", isLoading = false)
            }
        }
    }


    data class AddTreeState(
        val name: String = "",
        val type: String = "",
        val status: String = "",
        val coordinates: Pair<Double, Double> = Pair(0.0, 0.0),
        val imageUri: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
        val isSuccess: Boolean = false
    )
}