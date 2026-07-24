package com.example.resqai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqai.model.Shelter
import com.example.resqai.repository.ShelterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** UI state for the Shelter list screen. */
sealed class ShelterUiState {
    object Loading : ShelterUiState()
    data class Success(val shelters: List<Shelter>) : ShelterUiState()
    object Empty : ShelterUiState()
    data class Error(val message: String) : ShelterUiState()
}

/**
 * ViewModel for the Shelter screen.
 * Holds the search query and re-filters the dummy shelter list whenever it changes.
 */
class ShelterViewModel(
    private val repository: ShelterRepository = ShelterRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ShelterUiState>(ShelterUiState.Loading)
    val uiState: StateFlow<ShelterUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadShelters()
    }

    fun loadShelters() {
        viewModelScope.launch {
            _uiState.value = ShelterUiState.Loading
            try {
                val shelters = repository.getShelters()
                _uiState.value = if (shelters.isEmpty()) ShelterUiState.Empty else ShelterUiState.Success(shelters)
            } catch (e: Exception) {
                _uiState.value = ShelterUiState.Error(e.message ?: "Failed to load shelters")
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            try {
                val results = repository.searchShelters(query)
                _uiState.value = if (results.isEmpty()) ShelterUiState.Empty else ShelterUiState.Success(results)
            } catch (e: Exception) {
                _uiState.value = ShelterUiState.Error(e.message ?: "Search failed")
            }
        }
    }
}