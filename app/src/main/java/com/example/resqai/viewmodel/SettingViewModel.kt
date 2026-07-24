package com.example.resqai.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Supported in-app languages (dummy list — no real localization wired up). */
val supportedLanguages = listOf("English", "Hindi", "Spanish", "French")

/**
 * ViewModel for the Settings screen.
 * All preferences are held in-memory only (no DataStore/SharedPreferences) —
 * this is a frontend-only demo, so nothing persists across app restarts.
 */
class SettingsViewModel : ViewModel() {

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("English")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    private val _locationPermissionGranted = MutableStateFlow(true)
    val locationPermissionGranted: StateFlow<Boolean> = _locationPermissionGranted.asStateFlow()

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun toggleNotifications() {
        _notificationsEnabled.value = !_notificationsEnabled.value
    }

    fun onLanguageSelected(language: String) {
        _selectedLanguage.value = language
    }

    /** UI-only toggle simulating a permission grant/revoke — no real system permission call. */
    fun toggleLocationPermission() {
        _locationPermissionGranted.value = !_locationPermissionGranted.value
    }
}