package com.example.resqai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqai.model.EmergencyContact
import com.example.resqai.model.SOSRequest
import com.example.resqai.model.SOSStage
import com.example.resqai.repository.SOSRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** UI state for the SOS screen — mirrors [SOSStage] but as a sealed hierarchy for clarity. */
sealed class SosUiState {
    object Idle : SosUiState()
    object ConfirmDialogVisible : SosUiState()
    object Sending : SosUiState()
    data class Sent(val request: SOSRequest) : SosUiState()
    data class Error(val message: String) : SosUiState()
}

/**
 * ViewModel driving both the SOS screen (dialog → sending → success) and the
 * Rescue Status screen (timeline progression). Kept as one ViewModel since
 * Rescue Status is a direct continuation of the same SOS request.
 */
class SOSViewModel(
    private val repository: SOSRepository = SOSRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<SosUiState>(SosUiState.Idle)
    val uiState: StateFlow<SosUiState> = _uiState.asStateFlow()

    private val _currentStage = MutableStateFlow(SOSStage.IDLE)
    val currentStage: StateFlow<SOSStage> = _currentStage.asStateFlow()

    val emergencyContacts: List<EmergencyContact> = listOf(
        EmergencyContact(id = "c1", name = "Police", phoneNumber = "100"),
        EmergencyContact(id = "c2", name = "Fire Department", phoneNumber = "101"),
        EmergencyContact(id = "c3", name = "Ambulance / Hospital", phoneNumber = "102")
    )

    /** User tapped the big SOS button — show confirmation dialog. */
    fun onSosButtonPressed() {
        _uiState.value = SosUiState.ConfirmDialogVisible
    }

    /** User canceled the confirmation dialog. */
    fun onDismissDialog() {
        _uiState.value = SosUiState.Idle
    }

    /** User confirmed — simulate sending the SOS request. */
    fun onConfirmSos() {
        viewModelScope.launch {
            _uiState.value = SosUiState.Sending
            try {
                val request = repository.sendSosRequest()
                _currentStage.value = SOSStage.SENT
                _uiState.value = SosUiState.Sent(request)
            } catch (e: Exception) {
                _uiState.value = SosUiState.Error(e.message ?: "Failed to send SOS")
            }
        }
    }

    /** Resets the flow — used if the user wants to send a new SOS later. */
    fun reset() {
        _uiState.value = SosUiState.Idle
        _currentStage.value = SOSStage.IDLE
    }

    /** Simulates the rescue progressing automatically through remaining stages. */
    fun simulateRescueProgress() {
        viewModelScope.launch {
            if (_currentStage.value != SOSStage.SENT) return@launch
            _currentStage.value = repository.advanceToStage(SOSStage.RESPONDER_ASSIGNED)
            _currentStage.value = repository.advanceToStage(SOSStage.RESPONDER_EN_ROUTE)
            _currentStage.value = repository.advanceToStage(SOSStage.RESCUE_COMPLETED)
        }
    }
}