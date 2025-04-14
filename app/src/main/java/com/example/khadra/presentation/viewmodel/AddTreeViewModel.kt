package com.example.khadra.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khadra.data.model.Tree
import com.example.khadra.domain.usecase.AddTreeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class AddTreeViewModel @Inject constructor(
    private val addTreeUseCase: AddTreeUseCase,
    private val treeTypeRepository: com.example.khadra.data.repository.TreeTypeRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AddTreeState())
    val state: StateFlow<AddTreeState> = _state.asStateFlow()

    fun onEvent(event: AddTreeEvent) {
        when (event) {
            is AddTreeEvent.NameChanged -> {
                _state.value = _state.value.copy(
                    name = event.name,
                    error = if (isArabic(event.name)) null else "Tree name must be in Arabic"
                )
            }
            is AddTreeEvent.TypeSelected -> {
                viewModelScope.launch {
                    treeTypeRepository.getTreeTypes().collect { types ->
                        _state.value = _state.value.copy(
                            type = event.type,
                            error = if (types.any { it.name == event.type }) null else "Invalid tree type"
                        )
                    }
                }
            }
            is AddTreeEvent.StatusSelected -> {
                _state.value = _state.value.copy(
                    status = event.status,
                    error = if (event.status in listOf("Critical", "Low", "Moderate", "Healthy")) null else "Invalid status"
                )
            }
            is AddTreeEvent.CoordinatesChanged -> {
                _state.value = _state.value.copy(coordinates = Pair(event.latitude, event.longitude))
            }
            is AddTreeEvent.ImageUrlChanged -> {
                _state.value = _state.value.copy(
                    imageUrl = event.url,
                    error = if (isValidUrl(event.url)) null else "Invalid image URL"
                )
            }
            is AddTreeEvent.LocationChanged -> {
                _state.value = _state.value.copy(
                    location = event.location,
                    error = if (event.location.isNotBlank()) null else "Location must not be empty"
                )
            }

            is AddTreeEvent.Submit -> {
                if (canSubmit()) {
                    addTree()
                } else {
                    _state.value = _state.value.copy(error = "All fields must be correctly filled")
                }
            }
            is AddTreeEvent.ImageUriChanged -> {
                _state.value = _state.value.copy(
                    imageUri = event.uri,
                    error = null
                )
            }
        }
    }

    private fun canSubmit(): Boolean {
        val currentState = _state.value
        return currentState.name.isNotBlank() && currentState.imageUri!= Uri.EMPTY&&
                currentState.type.isNotBlank()&&
                currentState.type.lowercase() in listOf("fruit", "ornamental", "evergreen", "palm", "vegetable") &&
                currentState.status.lowercase() in listOf("critical", "low", "moderate", "healthy")&&
                currentState.status.isNotBlank()
               /* currentState.imageUrl.isNotBlank()*/
                /*isValidUrl(currentState.imageUrl)*/ && currentState.location.isNotBlank()
    }

    private fun addTree() {
        val currentState = _state.value
        _state.value = currentState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val tree = Tree(
                    id = UUID.randomUUID().toString(),
                    name = currentState.name,
                    type = currentState.type,
                    status = currentState.status,
                    location = currentState.location,
                    coordinates = currentState.coordinates,
                    urlImage = currentState.imageUrl,
                    lastIrrigationAction = currentState.dat, imageUri = currentState.imageUri,
                    createdAt = Date(),
                    updatedAt = Date()
                )
                addTreeUseCase(tree)
                _state.value = currentState.copy(isSuccess = true, isLoading = false)
            } catch (e: Exception) {
                _state.value = currentState.copy(error = "Failed to add tree", isLoading = false)
            }
        }
    }

    private fun isArabic(text: String): Boolean {
        return text.isNotEmpty() && Pattern.matches("^[\\u0600-\\u06FF\\s]+$", text)
    }

    private fun isValidUrl(url: String): Boolean {
        val regex = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$".toRegex()
        return regex.matches(url)
    }



    sealed class AddTreeEvent {
        data class NameChanged(val name: String) : AddTreeEvent()
        data class TypeSelected(val type: String) : AddTreeEvent()
        data class StatusSelected(val status: String) : AddTreeEvent()
        data class CoordinatesChanged(val latitude: Double, val longitude: Double) : AddTreeEvent()
        data class LocationChanged(val location:String) : AddTreeEvent()
        data class ImageUrlChanged(val url: String) : AddTreeEvent()
        data class ImageUriChanged(val uri: Uri) : AddTreeEvent()

        object Submit : AddTreeEvent()
    }

    // State Data Class
    data class AddTreeState(
        val name: String = "",
        val type: String = "",
        val status: String = "",
        val coordinates: Pair<Double, Double> = Pair(0.0, 0.0),
        val location: String = "",
        val imageUrl: String = "",val imageUri: Uri = Uri.EMPTY,
        val dat: Date = Date(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val isSuccess: Boolean = false
    )


}