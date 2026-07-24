package com.example.resqai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqai.model.Route
import com.example.resqai.repository.RouteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** UI state for the Safe Route screen. */
sealed class RouteUiState {
    object Loading : RouteUiState()
    data class Success(val route: Route) : RouteUiState()
    data class Error(val message: String) : RouteUiState()
}

/**
 * ViewModel for the Safe Route screen.
 * Loads the recommended evacuation route from [RouteRepository] (dummy data).
 */
class RouteViewModel(
    private val repository: RouteRepository = RouteRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<RouteUiState>(RouteUiState.Loading)
    val uiState: StateFlow<RouteUiState> = _uiState.asStateFlow()

    init {
        loadRoute()
    }

    fun loadRoute() {
        viewModelScope.launch {
            _uiState.value = RouteUiState.Loading
            try {
                val route = repository.getSafeRoute()
                _uiState.value = RouteUiState.Success(route)
            } catch (e: Exception) {
                _uiState.value = RouteUiState.Error(e.message ?: "Failed to load route")
            }
        }
    }
}