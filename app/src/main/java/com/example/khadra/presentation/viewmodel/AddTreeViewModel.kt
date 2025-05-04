package com.example.khadra.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khadra.data.model.Tree
import com.example.khadra.data.model.TreeType
import com.example.khadra.domain.usecase.AddTreeUseCase
import com.example.khadra.domain.usecase.GetTreeTypesUseCase
import com.example.khadra.util.LocationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddTreeViewModel @Inject constructor(
    private val addTreeUseCase: AddTreeUseCase,
    private val getTreeTypesUseCase: GetTreeTypesUseCase,
    private val locationHelper: LocationHelper
) : ViewModel() {

    private val _uiState = mutableStateOf(AddTreeUiState())
    val uiState: State<AddTreeUiState> = _uiState

    private val _addState = mutableStateOf<AddTreeState>(AddTreeState.Idle)
    val addState: State<AddTreeState> = _addState

    private val _treeTypes = MutableStateFlow<List<String>>(emptyList())
    val treeTypes: StateFlow<List<String>> = _treeTypes

    companion object {
        private const val TAG = "AddTreeViewModel"
    }

    init {
        loadTreeTypes()
    }

    private fun loadTreeTypes() {
        viewModelScope.launch {
            try {
                getTreeTypesUseCase().map { types -> 
                    types.map { it.name } 
                }.collect { names ->
                    _treeTypes.value = names
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading tree types", e)
            }
        }
    }

    fun getCurrentLocation() {
        if (!locationHelper.hasLocationPermission()) {
            _addState.value = AddTreeState.Error("يرجى السماح للتطبيق بالوصول إلى موقعك")
            return
        }

        viewModelScope.launch {
            try {
                locationHelper.getCurrentLocation().collect { location ->
                    location?.let {
                        Log.d(TAG, "Got location: ${it.latitude}, ${it.longitude}")
                        onEvent(AddTreeEvent.CoordinatesChanged(Pair(it.latitude, it.longitude)))
                    } ?: run {
                        Log.e(TAG, "Location is null")
                        _addState.value = AddTreeState.Error("لم نتمكن من تحديد موقعك")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting location", e)
                _addState.value = AddTreeState.Error("حدث خطأ أثناء تحديد موقعك")
            }
        }
    }

    fun hasLocationPermission(): Boolean {
        return locationHelper.hasLocationPermission()
    }

    fun onEvent(event: AddTreeEvent) {
        when (event) {
            is AddTreeEvent.NameChanged -> {
                _uiState.value = _uiState.value.copy(name = event.name)
            }
            is AddTreeEvent.TypeChanged -> {
                _uiState.value = _uiState.value.copy(type = event.type)
            }
            is AddTreeEvent.StatusChanged -> {
                _uiState.value = _uiState.value.copy(status = event.status)
            }
            is AddTreeEvent.LocationChanged -> {
                _uiState.value = _uiState.value.copy(location = event.location)
            }
            is AddTreeEvent.CoordinatesChanged -> {
                _uiState.value = _uiState.value.copy(coordinates = event.coordinates)
            }
            is AddTreeEvent.ImageSelected -> {
                _uiState.value = _uiState.value.copy(selectedImageUri = event.uri)
            }
            is AddTreeEvent.Submit -> {
                submitTree()
            }
        }
    }

    private fun submitTree() {
        viewModelScope.launch {
            val currentState = _uiState.value
            
            if (currentState.name.isEmpty()) {
                Log.w(TAG, "Validation failed: Name is empty")
                _addState.value = AddTreeState.Error("يرجى إدخال اسم الشجرة")
                return@launch
            }
            if (currentState.type.isNullOrEmpty()) {
                Log.w(TAG, "Validation failed: Type not selected")
                _addState.value = AddTreeState.Error("يرجى اختيار نوع الشجرة")
                return@launch
            }
            if (currentState.coordinates == null) {
                Log.w(TAG, "Validation failed: Coordinates not set")
                _addState.value = AddTreeState.Error("يرجى تحديد موقع الشجرة على الخريطة")
                return@launch
            }

            try {
                _addState.value = AddTreeState.Loading
                
                val now = Date()
                val tree = Tree.fromCoordinates(
                    id = UUID.randomUUID().toString(),
                    name = currentState.name,
                    type = currentState.type,
                    status = currentState.status,
                    coordinates = currentState.coordinates,
                    location = currentState.location,
                    lastIrrigationAction = now,
                    createdAt = now,
                    updatedAt = now
                )
                
                Log.d(TAG, "Created tree object: $tree")
                Log.d(TAG, "Selected image URI: ${currentState.selectedImageUri}")

                val addedTree = addTreeUseCase(tree, currentState.selectedImageUri)
                Log.d(TAG, "Tree added successfully: $addedTree")
                
                _addState.value = AddTreeState.Success
                
                _uiState.value = AddTreeUiState()
            } catch (e: Exception) {
                Log.e(TAG, "Error adding tree", e)
                _addState.value = AddTreeState.Error(e.message ?: "حدث خطأ أثناء إضافة الشجرة")
            }
        }
    }

    fun resetAddState() {
        Log.d(TAG, "Resetting add state")
        _addState.value = AddTreeState.Idle
    }
}

data class AddTreeUiState(
    val name: String = "",
    val type: String? = null,
    val status: String = "",
    val location: String = "",
    val coordinates: Pair<Double, Double>? = null,
    val selectedImageUri: Uri? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

sealed class AddTreeState {
    object Idle : AddTreeState()
    object Loading : AddTreeState()
    object Success : AddTreeState()
    data class Error(val message: String) : AddTreeState()
}

sealed class AddTreeEvent {
    data class NameChanged(val name: String) : AddTreeEvent()
    data class TypeChanged(val type: String) : AddTreeEvent()
    data class StatusChanged(val status: String) : AddTreeEvent()
    data class LocationChanged(val location: String) : AddTreeEvent()
    data class CoordinatesChanged(val coordinates: Pair<Double, Double>) : AddTreeEvent()
    data class ImageSelected(val uri: Uri) : AddTreeEvent()
    object Submit : AddTreeEvent()
}
