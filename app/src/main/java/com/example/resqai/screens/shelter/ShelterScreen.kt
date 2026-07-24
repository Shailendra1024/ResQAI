package com.example.resqai.screens.shelter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.resqai.components.ResQSearchBar
import com.example.resqai.components.ResQTopAppBar
import com.example.resqai.components.ShelterCard
import com.example.resqai.viewmodel.ShelterUiState
import com.example.resqai.viewmodel.ShelterViewModel

/**
 * Shelter screen — search bar + list of nearby shelters with
 * capacity, distance, and availability info.
 */
@Composable
fun ShelterScreen(
    viewModel: ShelterViewModel = remember { ShelterViewModel() },
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            ResQTopAppBar(
                title = "Nearby Shelters",
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ResQSearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                placeholder = "Search shelters by name or area",
                modifier = Modifier.padding(16.dp)
            )

            when (val shelterState = state) {
                is ShelterUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is ShelterUiState.Empty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No shelters match your search",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                is ShelterUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: ${shelterState.message}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                is ShelterUiState.Success -> {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(shelterState.shelters, key = { it.id }) { shelter ->
                            ShelterCard(
                                shelter = shelter,
                                onNavigateClick = { /* Dummy — no real routing to this shelter yet */ }
                            )
                        }
                    }
                }
            }
        }
    }
}