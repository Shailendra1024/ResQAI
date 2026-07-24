package com.example.resqai.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resqai.model.Alert
import com.example.resqai.model.AlertSeverity
import com.example.resqai.model.AlertStatus
import com.example.resqai.ui.theme.ResQAITheme

/**
 * Reusable card representing a single disaster alert.
 * Used in both the Home "Latest Alerts" list and the full Alert List screen.
 */
@Composable
fun AlertCard(
    alert: Alert,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = null,
                        tint = severityColor(alert.severity),
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = alert.disasterType,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                StatusChip(text = alert.severity.name, color = severityColor(alert.severity))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = alert.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Text(
                    text = " · ${alert.time}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                StatusChip(text = statusLabel(alert.status), color = statusColor(alert.status))
            }
        }
    }
}

private fun statusLabel(status: AlertStatus): String = when (status) {
    AlertStatus.ACTIVE -> "Active"
    AlertStatus.MONITORING -> "Monitoring"
    AlertStatus.RESOLVED -> "Resolved"
}

@Preview(showBackground = true)
@Composable
private fun AlertCardPreview() {
    ResQAITheme {
        AlertCard(
            alert = Alert(
                id = "1",
                disasterType = "Flood Warning",
                severity = AlertSeverity.HIGH,
                location = "Riverside District",
                time = "10 min ago",
                status = AlertStatus.ACTIVE
            ),
            onClick = {}
        )
    }
}