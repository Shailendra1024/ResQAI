package com.example.resqai.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resqai.components.BottomNavItem
import com.example.resqai.components.ResQBottomNavigationBar
import com.example.resqai.components.ResQTopAppBar
import com.example.resqai.components.StatusChip
import com.example.resqai.components.severityColor
import com.example.resqai.model.Alert
import com.example.resqai.model.EmergencyContact
import com.example.resqai.model.Shelter
import com.example.resqai.repository.WeatherInfo
import com.example.resqai.ui.theme.ResQAITheme
import com.example.resqai.viewmodel.HomeUiState
import com.example.resqai.viewmodel.HomeViewModel

/**
 * Home Dashboard — the app's landing screen after login/register.
 * Shows risk status, weather, shelters, emergency contacts, latest alerts,
 * a quick-action grid, and hosts the bottom navigation bar.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = remember { HomeViewModel() },
    onNavigateToAlerts: () -> Unit,
    onNavigateToSafeRoute: () -> Unit,
    onNavigateToShelters: () -> Unit,
    onNavigateToSos: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ResQTopAppBar(title = "ResQAI", showNotificationIcon = true)
        },
        bottomBar = {
            ResQBottomNavigationBar(
                currentItem = BottomNavItem.Home,
                onItemSelected = { item ->
                    when (item) {
                        BottomNavItem.Home -> Unit
                        BottomNavItem.Map -> onNavigateToSafeRoute()
                        BottomNavItem.Sos -> onNavigateToSos()
                        BottomNavItem.History -> onNavigateToHistory()
                        BottomNavItem.Profile -> onNavigateToProfile()
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is HomeUiState.Loading -> LoadingState(paddingValues)
            is HomeUiState.Error -> ErrorState(paddingValues, state.message)
            is HomeUiState.Success -> HomeContent(
                paddingValues = paddingValues,
                riskStatus = state.riskStatus,
                weather = state.weather,
                shelters = state.shelters,
                contacts = state.emergencyContacts,
                alerts = state.latestAlerts,
                onNavigateToAlerts = onNavigateToAlerts,
                onNavigateToSafeRoute = onNavigateToSafeRoute,
                onNavigateToShelters = onNavigateToShelters,
                onNavigateToSos = onNavigateToSos,
                onNavigateToHistory = onNavigateToHistory,
                onNavigateToProfile = onNavigateToProfile
            )
        }
    }
}

@Composable
private fun LoadingState(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(paddingValues: PaddingValues, message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error: $message", color = MaterialTheme.colorScheme.error)
    }
}

@Composable
private fun HomeContent(
    paddingValues: PaddingValues,
    riskStatus: String,
    weather: WeatherInfo,
    shelters: List<Shelter>,
    contacts: List<EmergencyContact>,
    alerts: List<Alert>,
    onNavigateToAlerts: () -> Unit,
    onNavigateToSafeRoute: () -> Unit,
    onNavigateToShelters: () -> Unit,
    onNavigateToSos: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp) ,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RiskStatusCard(riskStatus)
        WeatherCard(weather)
        NearbySheltersCard(shelters, onSeeAll = onNavigateToShelters)
        EmergencyContactsCard(contacts)
        LatestAlertsCard(alerts, onSeeAll = onNavigateToAlerts)

        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        QuickActionGrid(
            onSos = onNavigateToSos,
            onSafeRoute = onNavigateToSafeRoute,
            onShelter = onNavigateToShelters,
            onDisasterMap = onNavigateToSafeRoute,
            onHistory = onNavigateToHistory,
            onProfile = onNavigateToProfile
        )
    }
}

/** Card showing the current overall disaster risk level for the user's area. */
@Composable
private fun RiskStatusCard(riskStatus: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Shield,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = "Current Risk Status",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = riskStatus,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/** Card showing today's local weather snapshot. */
@Composable
private fun WeatherCard(weather: WeatherInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Today's Weather",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${weather.temperatureC}°C",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = weather.condition, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Humidity ${weather.humidity}% · Wind ${weather.windKmh} km/h",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/** Card listing the nearest 2 shelters, with a "See all" action. */
@Composable
private fun NearbySheltersCard(shelters: List<Shelter>, onSeeAll: () -> Unit) {
    DashboardCard(title = "Nearby Shelters", onSeeAll = onSeeAll) {
        shelters.take(2).forEach { shelter ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = shelter.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    Text(
                        text = "${shelter.distanceKm} km · ${shelter.availableSpots} spots left",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                StatusChip(
                    text = if (shelter.isAvailable) "Available" else "Full",
                    color = if (shelter.isAvailable) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
            }
        }
    }
}

/** Card listing emergency service contacts (Police, Fire, Ambulance). */
@Composable
private fun EmergencyContactsCard(contacts: List<EmergencyContact>) {
    DashboardCard(title = "Emergency Contacts", onSeeAll = null) {
        contacts.forEach { contact ->
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
                    Text(
                        text = contact.name,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Text(
                    text = contact.phoneNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/** Card listing the 2 most recent disaster alerts, with a "See all" action. */
@Composable
private fun LatestAlertsCard(alerts: List<Alert>, onSeeAll: () -> Unit) {
    DashboardCard(title = "Latest Alerts", onSeeAll = onSeeAll) {
        alerts.take(2).forEach { alert ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = null,
                        tint = severityColor(alert.severity),
                        modifier = Modifier.size(18.dp)
                    )
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(text = alert.disasterType, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                        Text(
                            text = "${alert.location} · ${alert.time}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                StatusChip(text = alert.severity.name, color = severityColor(alert.severity))
            }
        }
    }
}

/** Shared card shell used by all dashboard list-style cards (title + optional "See all"). */
@Composable
private fun DashboardCard(
    title: String,
    onSeeAll: (() -> Unit)?,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                if (onSeeAll != null) {
                    Text(
                        text = "See all",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
            Box(modifier = Modifier.padding(top = 8.dp)) {
                Column { content() }
            }
        }
    }
}

/** Data for a single quick-action grid tile. */
private data class QuickAction(val label: String, val icon: ImageVector, val onClick: () -> Unit)

/** 3x2 grid of quick-access shortcuts to the app's core features. */
@Composable
private fun QuickActionGrid(
    onSos: () -> Unit,
    onSafeRoute: () -> Unit,
    onShelter: () -> Unit,
    onDisasterMap: () -> Unit,
    onHistory: () -> Unit,
    onProfile: () -> Unit
) {
    val actions = listOf(
        QuickAction("SOS", Icons.Filled.Sos, onSos),
        QuickAction("Safe Route", Icons.Filled.Map, onSafeRoute),
        QuickAction("Shelter", Icons.Filled.Home, onShelter),
        QuickAction("Disaster Map", Icons.Filled.Map, onDisasterMap),
        QuickAction("History", Icons.Filled.History, onHistory),
        QuickAction("Profile", Icons.Filled.Person, onProfile)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(actions) { action ->
            QuickActionTile(action)
        }
    }
}

@Composable
private fun QuickActionTile(action: QuickAction) {
    Surface(
        onClick = action.onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = action.label,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    ResQAITheme {
        HomeScreen(
            onNavigateToAlerts = {},
            onNavigateToSafeRoute = {},
            onNavigateToShelters = {},
            onNavigateToSos = {},
            onNavigateToHistory = {},
            onNavigateToProfile = {}
        )
    }
}