package com.example.khadra.presentation.viewmodel

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khadra.data.model.User
import com.example.khadra.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    init {
        getCurrentUser()
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                authRepository.signUp(email, password)
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign up failed")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                authRepository.login(email, password)
                getCurrentUser()
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                authRepository.logout()
                _currentUser.value = null
                _authState.value = AuthState.Initial
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Logout failed")
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                authRepository.resetPassword(email)
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Password reset failed")
            }
        }
    }

    fun updateProfilePicture(imageUri: Uri) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                authRepository.updateProfilePicture(imageUri)
                getCurrentUser()
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Failed to update profile picture")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentUser() {
        viewModelScope.launch {
            try {
                _currentUser.value = authRepository.getCurrentUser()
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Failed to get current user")
            }
        }
    }
}