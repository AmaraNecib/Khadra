package com.example.khadra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddTreeViewModel : ViewModel() {

    private val _state = MutableStateFlow(AddTreeState())
    val state: StateFlow<AddTreeState> = _state

    fun onEvent(event: AddTreeEvent) {
        when (event) {
            is AddTreeEvent.EnterName -> _state.value = _state.value.copy(name = event.name)
            is AddTreeEvent.EnterType -> _state.value = _state.value.copy(type = event.type)
            is AddTreeEvent.EnterStatus -> _state.value = _state.value.copy(status = event.status)
            is AddTreeEvent.EnterLatitude -> _state.value = _state.value.copy(latitude = event.latitude)
            is AddTreeEvent.EnterLongitude -> _state.value = _state.value.copy(longitude = event.longitude)
            is AddTreeEvent.EnterImageUrl -> _state.value = _state.value.copy(imageUrl = event.imageUrl)
            AddTreeEvent.Submit -> submitTree()
        }
    }

    private fun submitTree() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, isSuccess = false, errorMessage = null)
            // Simulate success
            kotlinx.coroutines.delay(1000)
            _state.value = _state.value.copy(isLoading = false, isSuccess = true)
        }
    }
}
