package com.example.resqai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqai.model.User
import com.example.resqai.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** UI state for the Profile screen. */
sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val user: User) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

/**
 * ViewModel for the Profile screen.
 * Supports a simple edit-mode: toggling [isEditing] reveals editable fields;
 * [saveProfile] simulates persisting changes via [ProfileRepository].
 */
class ProfileViewModel(
    private val repository: ProfileRepository = ProfileRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                val user = repository.getUserProfile()
                _uiState.value = ProfileUiState.Success(user)
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Failed to load profile")
            }
        }
    }

    fun toggleEditMode() {
        _isEditing.value = !_isEditing.value
    }

    /** Updates a single field on the in-memory user while in edit mode. */
    fun updateField(update: (User) -> User) {
        val current = _uiState.value
        if (current is ProfileUiState.Success) {
            _uiState.value = ProfileUiState.Success(update(current.user))
        }
    }

    fun saveProfile() {
        val current = _uiState.value
        if (current !is ProfileUiState.Success) return

        viewModelScope.launch {
            _isSaving.value = true
            try {
                val saved = repository.updateUserProfile(current.user)
                _uiState.value = ProfileUiState.Success(saved)
                _isEditing.value = false
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Failed to save profile")
            } finally {
                _isSaving.value = false
            }
        }
    }
}