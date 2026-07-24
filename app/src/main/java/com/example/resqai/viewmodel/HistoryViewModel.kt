package com.example.resqai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqai.model.Alert
import com.example.resqai.model.AlertSeverity
import com.example.resqai.repository.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Filter chip options for the History screen. */
enum class HistoryFilter(val label: String) {
    ALL("All"),
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low")
}

/** UI state for the Alert History screen. */
sealed class HistoryUiState {
    object Loading : HistoryUiState()
    data class Success(val alerts: List<Alert>) : HistoryUiState()
    object Empty : HistoryUiState()
    data class Error(val message: String) : HistoryUiState()
}

/**
 * ViewModel for the Alert History screen.
 * Loads the full dummy history once, then applies search + severity filter
 * client-side (in-memory) whenever either changes.
 */
class HistoryViewModel(
    private val repository: HistoryRepository = HistoryRepository()
) : ViewModel() {

    private var allAlerts: List<Alert> = emptyList()

    private val _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow(HistoryFilter.ALL)
    val selectedFilter: StateFlow<HistoryFilter> = _selectedFilter.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _uiState.value = HistoryUiState.Loading
            try {
                allAlerts = repository.getHistory()
                applyFilters()
            } catch (e: Exception) {
                _uiState.value = HistoryUiState.Error(e.message ?: "Failed to load history")
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun onFilterSelected(filter: HistoryFilter) {
        _selectedFilter.value = filter
        applyFilters()
    }

    /** Applies both search query and severity filter to the in-memory alert list. */
    private fun applyFilters() {
        val query = _searchQuery.value
        val filter = _selectedFilter.value

        var result = allAlerts

        if (filter != HistoryFilter.ALL) {
            val severity = when (filter) {
                HistoryFilter.HIGH -> AlertSeverity.HIGH
                HistoryFilter.MEDIUM -> AlertSeverity.MEDIUM
                HistoryFilter.LOW -> AlertSeverity.LOW
                HistoryFilter.ALL -> null
            }
            result = result.filter { it.severity == severity }
        }

        if (query.isNotBlank()) {
            result = result.filter {
                it.disasterType.contains(query, ignoreCase = true) ||
                        it.location.contains(query, ignoreCase = true)
            }
        }

        _uiState.value = if (result.isEmpty()) HistoryUiState.Empty else HistoryUiState.Success(result)
    }
}