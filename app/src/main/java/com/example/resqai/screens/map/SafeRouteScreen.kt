package com.example.resqai.screens.map

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resqai.components.PrimaryButton
import com.example.resqai.components.ResQTopAppBar
import com.example.resqai.model.Route
import com.example.resqai.model.SafetyTier
import com.example.resqai.ui.theme.ResQAITheme
import com.example.resqai.ui.theme.SeverityHigh
import com.example.resqai.ui.theme.SeverityMedium
import com.example.resqai.ui.theme.StatusSafe
import com.example.resqai.viewmodel.RouteUiState
import com.example.resqai.viewmodel.RouteViewModel

/**
 * Safe Route screen — shows the recommended evacuation route from the
 * user's current location to the nearest safe shelter.
 */
@Composable
fun SafeRouteScreen(
    viewModel: RouteViewModel = remember { RouteViewModel() },
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ResQTopAppBar(
                title = "Safe Route",
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        when (val routeState = state) {
            is RouteUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is RouteUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${routeState.message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            is RouteUiState.Success -> {
                SafeRouteContent(
                    route = routeState.route,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun SafeRouteContent(route: Route, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MapPlaceholder()

        LocationCard(
            icon = Icons.Filled.MyLocation,
            iconTint = MaterialTheme.colorScheme.secondary,
            label = "Current Location",
            value = route.originLabel
        )

        LocationCard(
            icon = Icons.Filled.Place,
            iconTint = MaterialTheme.colorScheme.primary,
            label = "Destination Shelter",
            value = route.destinationShelterName,
            subValue = route.destinationAddress
        )

        RouteStatsRow(route)

        if (route.routeSummary.isNotBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = route.routeSummary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(14.dp)
                )
            }
        }

        PrimaryButton(
            text = "Start Navigation",
            onClick = { /* Dummy — no real turn-by-turn navigation yet */ },
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
        )
    }
}

/** Google-Map-style placeholder — replace with a real map SDK integration later. */
@Composable
private fun MapPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 10f)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.large),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Map,
                contentDescription = "Map placeholder",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Map view placeholder",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/** Shared card layout for both the "Current Location" and "Destination Shelter" rows. */
@Composable
private fun LocationCard(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
    subValue: String? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                subValue?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/** Row of 3 stat tiles: ETA, distance, safety score. */
@Composable
private fun RouteStatsRow(route: Route) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatTile(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.Schedule,
            label = "ETA",
            value = "${route.estimatedMinutes} min"
        )
        StatTile(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.Straighten,
            label = "Distance",
            value = "${route.distanceKm} km"
        )
        StatTile(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.Shield,
            label = "Safety",
            value = "${route.safetyScore}",
            valueColor = safetyTierColor(route.safetyTier)
        )
    }
}

@Composable
private fun StatTile(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 14.dp, horizontal = 8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = valueColor,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun safetyTierColor(tier: SafetyTier): Color = when (tier) {
    SafetyTier.SAFE -> StatusSafe
    SafetyTier.MODERATE -> SeverityMedium
    SafetyTier.RISKY -> SeverityHigh
}

@Preview(showBackground = true)
@Composable
private fun SafeRouteScreenPreview() {
    ResQAITheme {
        SafeRouteScreen(onBackClick = {})
    }
}