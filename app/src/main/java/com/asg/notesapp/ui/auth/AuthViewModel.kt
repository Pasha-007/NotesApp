package com.asg.notesapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asg.notesapp.data.model.User
import com.asg.notesapp.data.repository.AuthRepository
import com.asg.notesapp.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val authState: StateFlow<UiState<User>> = _authState.asStateFlow()

    fun signUp(name: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _authState.value = UiState.Loading

            // Validation
            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                _authState.value = UiState.Error("All fields are required")
                return@launch
            }

            if (password != confirmPassword) {
                _authState.value = UiState.Error("Passwords don't match")
                return@launch
            }

            if (password.length < 8) {
                _authState.value = UiState.Error("Password must be at least 8 characters")
                return@launch
            }

            val result = authRepository.signUp(name, email, password)

            if (result.isSuccess) {
                _authState.value = UiState.Success(result.getOrNull()!!)
            } else {
                _authState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Sign up failed")
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = UiState.Loading

            if (email.isBlank() || password.isBlank()) {
                _authState.value = UiState.Error("Email and password are required")
                return@launch
            }

            val result = authRepository.signIn(email, password)

            if (result.isSuccess) {
                _authState.value = UiState.Success(result.getOrNull()!!)
            } else {
                _authState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Sign in failed")
            }
        }
    }

    fun resetAuthState() {
        _authState.value = UiState.Idle
    }
}