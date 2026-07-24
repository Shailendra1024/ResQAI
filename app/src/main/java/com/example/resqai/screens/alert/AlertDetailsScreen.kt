package com.example.resqai.screens.alert

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.resqai.components.PrimaryButton
import com.example.resqai.components.ResQTopAppBar
import com.example.resqai.components.SecondaryButton
import com.example.resqai.components.StatusChip
import com.example.resqai.components.severityColor
import com.example.resqai.model.Alert
import com.example.resqai.viewmodel.AlertDetailsUiState
import com.example.resqai.viewmodel.AlertViewModel

/**
 * Full details for a single alert: header, image placeholder, description,
 * affected area, recommended actions, emergency contacts, and action buttons.
 */
@Composable
fun AlertDetailsScreen(
    alertId: String,
    viewModel: AlertViewModel = remember { AlertViewModel() },
    onBackClick: () -> Unit
) {
    val state by viewModel.detailsState.collectAsState()

    LaunchedEffect(alertId) {
        viewModel.loadAlertDetails(alertId)
    }

    Scaffold(
        topBar = {
            ResQTopAppBar(
                title = "Alert Details",
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        when (val detailsState = state) {
            is AlertDetailsUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is AlertDetailsUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = detailsState.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            is AlertDetailsUiState.Success -> {
                AlertDetailsContent(
                    alert = detailsState.alert,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun AlertDetailsContent(alert: Alert, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Disaster image placeholder — replace with real image loading later
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Image,
                contentDescription = "Disaster image placeholder",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
        }

        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    tint = severityColor(alert.severity),
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = alert.disasterType,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            Row(
                modifier = Modifier.padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusChip(text = alert.severity.name, color = severityColor(alert.severity))
                StatusChip(text = alert.location, color = MaterialTheme.colorScheme.onSurfaceVariant)
                StatusChip(text = alert.time, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            SectionTitle("Description")
            Text(
                text = alert.description.ifBlank { "No description available." },
                style = MaterialTheme.typography.bodyMedium
            )

            SectionTitle("Affected Area")
            Text(
                text = alert.affectedArea.ifBlank { "Not specified." },
                style = MaterialTheme.typography.bodyMedium
            )

            SectionTitle("Recommended Actions")
            if (alert.recommendedActions.isEmpty()) {
                Text(
                    text = "No specific recommendations at this time.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                alert.recommendedActions.forEach { action ->
                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(text = "•  ", style = MaterialTheme.typography.bodyMedium)
                        Text(text = action, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            SectionTitle("Emergency Contacts")
            EmergencyContactRow(name = "Police", number = "100")
            EmergencyContactRow(name = "Fire Department", number = "101")
            EmergencyContactRow(name = "Ambulance", number = "102")

            PrimaryButton(
                text = "Evacuate Now",
                onClick = { /* Dummy — no real evacuation trigger */ },
                modifier = Modifier.padding(top = 24.dp)
            )

            SecondaryButton(
                text = "Share Alert",
                onClick = { /* Dummy — no real share intent yet */ },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 6.dp)
                    )
                },
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
    )
}

@Composable
private fun EmergencyContactRow(name: String, number: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Phone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Text(text = name, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 8.dp))
        }
        Text(
            text = number,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}