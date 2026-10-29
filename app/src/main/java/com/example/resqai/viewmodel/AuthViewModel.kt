package com.example.resqai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqai.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Login and Register screens.
 * Calls the real backend through [AuthRepository]; on success the JWT token
 * is saved in SessionStore (done inside AuthRepository.login).
 */
class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun clearError() {
        _error.value = null
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        if (_isLoading.value) return
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.login(email.trim(), password)
            _isLoading.value = false
            result
                .onSuccess { onSuccess() }
                .onFailure { _error.value = it.message ?: "Login failed. Please try again." }
        }
    }

    /** Registers the account, then logs in right away so the token is saved. */
    fun register(name: String, email: String, password: String, onSuccess: () -> Unit) {
        if (_isLoading.value) return
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val registered = repository.register(name.trim(), email.trim(), password)
            if (registered.isFailure) {
                _isLoading.value = false
                _error.value = registered.exceptionOrNull()?.message
                    ?: "Registration failed. Please try again."
                return@launch
            }

            val loggedIn = repository.login(email.trim(), password)
            _isLoading.value = false
            loggedIn
                .onSuccess { onSuccess() }
                .onFailure {
                    _error.value = it.message ?: "Account created, but login failed. Please log in."
                }
        }
    }
}
