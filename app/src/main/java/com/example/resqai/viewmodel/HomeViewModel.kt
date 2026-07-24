package com.example.resqai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqai.model.Alert
import com.example.resqai.model.EmergencyContact
import com.example.resqai.model.Shelter
import com.example.resqai.repository.HomeRepository
import com.example.resqai.repository.WeatherInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Sealed UI state for the Home Dashboard, following Loading/Success/Error/Empty pattern. */
sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val riskStatus: String,
        val weather: WeatherInfo,
        val shelters: List<Shelter>,
        val emergencyContacts: List<EmergencyContact>,
        val latestAlerts: List<Alert>
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

/**
 * ViewModel for the Home Dashboard screen.
 * Loads all dashboard data from [HomeRepository] (dummy data) on init
 * and exposes it as a single [StateFlow] of [HomeUiState].
 *
 * NOTE: In a real app this would be injected (Hilt/Koin). Since this is
 * frontend-only, it's instantiated directly via a factory in the NavGraph.
 */
class HomeViewModel(
    private val repository: HomeRepository = HomeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val riskStatus = repository.getRiskStatus()
                val weather = repository.getWeather()
                val shelters = repository.getNearbyShelters()
                val contacts = repository.getEmergencyContacts()
                val alerts = repository.getLatestAlerts()

                _uiState.value = HomeUiState.Success(
                    riskStatus = riskStatus,
                    weather = weather,
                    shelters = shelters,
                    emergencyContacts = contacts,
                    latestAlerts = alerts
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }
}