package com.example.resqai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqai.model.Alert
import com.example.resqai.repository.AlertRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** UI state for the Alert List screen. */
sealed class AlertListUiState {
    object Loading : AlertListUiState()
    data class Success(val alerts: List<Alert>) : AlertListUiState()
    object Empty : AlertListUiState()
    data class Error(val message: String) : AlertListUiState()
}

/** UI state for the Alert Details screen. */
sealed class AlertDetailsUiState {
    object Loading : AlertDetailsUiState()
    data class Success(val alert: Alert) : AlertDetailsUiState()
    data class Error(val message: String) : AlertDetailsUiState()
}

/**
 * Shared ViewModel for both the Alert List and Alert Details screens.
 * Kept as one ViewModel (scoped per-screen via NavGraph) since Details
 * simply looks up an alert already loaded by the List — avoids a second
 * redundant "loading" flicker for dummy data that's already in memory.
 */
class AlertViewModel(
    private val repository: AlertRepository = AlertRepository()
) : ViewModel() {

    private val _listState = MutableStateFlow<AlertListUiState>(AlertListUiState.Loading)
    val listState: StateFlow<AlertListUiState> = _listState.asStateFlow()

    private val _detailsState = MutableStateFlow<AlertDetailsUiState>(AlertDetailsUiState.Loading)
    val detailsState: StateFlow<AlertDetailsUiState> = _detailsState.asStateFlow()

    init {
        loadAlerts()
    }

    fun loadAlerts() {
        viewModelScope.launch {
            _listState.value = AlertListUiState.Loading
            try {
                val alerts = repository.getAlerts()
                _listState.value = if (alerts.isEmpty()) {
                    AlertListUiState.Empty
                } else {
                    AlertListUiState.Success(alerts)
                }
            } catch (e: Exception) {
                _listState.value = AlertListUiState.Error(e.message ?: "Failed to load alerts")
            }
        }
    }

    fun loadAlertDetails(alertId: String) {
        viewModelScope.launch {
            _detailsState.value = AlertDetailsUiState.Loading
            try {
                val alert = repository.getAlertById(alertId)
                _detailsState.value = if (alert != null) {
                    AlertDetailsUiState.Success(alert)
                } else {
                    AlertDetailsUiState.Error("Alert not found")
                }
            } catch (e: Exception) {
                _detailsState.value = AlertDetailsUiState.Error(e.message ?: "Failed to load alert")
            }
        }
    }
}