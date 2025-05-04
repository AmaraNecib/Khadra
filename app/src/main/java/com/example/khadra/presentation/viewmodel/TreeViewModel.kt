package com.example.khadra.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khadra.data.model.IrrigationHistory
import com.example.khadra.data.model.Location
import com.example.khadra.data.model.Tree
import com.example.khadra.data.repository.TreeRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import android.location.Geocoder
import android.os.Build
import android.location.Address
import android.util.Log

data class TreeUiState(
    val trees: List<Tree> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TreeViewModel @Inject constructor(
    private val repository: TreeRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(TreeUiState())
    val uiState: StateFlow<TreeUiState> = _uiState.asStateFlow()

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val geocoder = Geocoder(context, Locale("ar"))

    fun getCurrentLocation(onLocationReceived: (Location) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    onLocationReceived(Location(it.latitude, it.longitude))
                }
            }
            .addOnFailureListener { e ->
                _uiState.value = _uiState.value.copy(error = "Error getting location: ${e.message}")
            }
    }

    fun getAddressFromLocation(latitude: Double, longitude: Double, onAddressReceived: (String) -> Unit) {
        viewModelScope.launch {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                        val address = if (addresses.isNotEmpty()) {
                            addresses[0].let { addr ->
                                buildString {
                                    append(addr.locality ?: "")
                                    append(", ")
                                    append(addr.adminArea ?: "")
                                    append(", ")
                                    append(addr.countryName ?: "")
                                }
                            }
                        } else ""
                        onAddressReceived(address)
                    }
                } else {
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                    val address = if (!addresses.isNullOrEmpty()) {
                        addresses[0].let { addr ->
                            buildString {
                                append(addr.locality ?: "")
                                append(", ")
                                append(addr.adminArea ?: "")
                                append(", ")
                                append(addr.countryName ?: "")
                            }
                        }
                    } else ""
                    onAddressReceived(address)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Error getting address: ${e.message}")
                onAddressReceived("")
            }
        }
    }

    fun addTree(tree: Tree, imageUri: Uri? = null) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                repository.addTree(tree, imageUri)
                loadTrees()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Error adding tree: ${e.message}")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun updateTree(tree: Tree) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val updatedTree = repository.updateTree(tree)
                _uiState.update { currentState ->
                    currentState.copy(
                        trees = currentState.trees.map { 
                            if (it.id == tree.id) updatedTree else it 
                        },
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("TreeViewModel", "Error updating tree", e)
                _uiState.value = _uiState.value.copy(
                    error = "Error updating tree: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun loadTrees() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val trees = repository.getTrees()
                _uiState.update { it.copy(
                    trees = trees,
                    isLoading = false,
                    error = null
                ) }
            } catch (e: Exception) {
                Log.e("TreeViewModel", "Error loading trees", e)
                _uiState.value = _uiState.value.copy(
                    error = "Error loading trees: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun addIrrigationHistory(treeId: String, notes: String? = null) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val irrigationHistory = IrrigationHistory(
                    treeId = treeId,
                    irrigationDate = Date(),
                    notes = notes
                )
                val updatedHistory = repository.addIrrigationHistory(irrigationHistory)
                
                _uiState.update { currentState ->
                    currentState.copy(
                        trees = currentState.trees.map { tree ->
                            if (tree.id == treeId) {
                                tree.copy(
                                    irrigationHistory = (tree.irrigationHistory.orEmpty() + updatedHistory),
                                    lastIrrigationAction = updatedHistory.irrigationDate,
                                    status = "healthy",
                                    updatedAt = Date()
                                )
                            } else tree
                        },
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error updating irrigation history: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun loadTreeIrrigationHistory(treeId: String) {
        viewModelScope.launch {
            try {
                val history = repository.getIrrigationHistory(treeId)
                _uiState.update { currentState ->
                    currentState.copy(
                        trees = currentState.trees.map { tree ->
                            if (tree.id == treeId) {
                                tree.copy(irrigationHistory = history)
                            } else tree
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun deleteTree(treeId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                repository.deleteTree(treeId)
                _uiState.update { currentState ->
                    currentState.copy(
                        trees = currentState.trees.filterNot { it.id == treeId },
                        isLoading = false
                    )
                }
                onSuccess()
            } catch (e: Exception) {
                Log.e("TreeViewModel", "Error deleting tree", e)
                _uiState.value = _uiState.value.copy(
                    error = "Error deleting tree: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    init {
        loadTrees()
    }
}
