package com.example.resqai.screens.alert

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.example.resqai.components.AlertCard
import com.example.resqai.components.ResQTopAppBar
import com.example.resqai.viewmodel.AlertListUiState
import com.example.resqai.viewmodel.AlertViewModel

@Composable
fun AlertListScreen(
    viewModel: AlertViewModel = remember { AlertViewModel() },
    onBackClick: () -> Unit,
    onAlertClick: (String) -> Unit
) {
    val state by viewModel.listState.collectAsState()

    Scaffold(
        topBar = {
            ResQTopAppBar(
                title = "Disaster Alerts",
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        when (val listState = state) {
            is AlertListUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is AlertListUiState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No active alerts right now",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            is AlertListUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${listState.message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            is AlertListUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(listState.alerts, key = { it.id }) { alert ->
                        AlertCard(
                            alert = alert,
                            onClick = { onAlertClick(alert.id) }
                        )
                    }
                }
            }
        }
    }
}