package com.example.resqai.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resqai.ui.theme.ResQAITheme
import com.example.resqai.ui.theme.SeverityHigh
import com.example.resqai.ui.theme.SeverityLow
import com.example.resqai.ui.theme.SeverityMedium
import com.example.resqai.ui.theme.StatusInfo
import com.example.resqai.ui.theme.StatusSafe

/**
 * Small colored badge used for severity levels and status labels
 * (e.g. "HIGH", "Active", "Monitoring", "Available").
 */
@Composable
fun StatusChip(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

/** Maps [com.example.resqai.model.AlertSeverity] to its display color — used by AlertCard. */
fun severityColor(severity: com.example.resqai.model.AlertSeverity): Color = when (severity) {
    com.example.resqai.model.AlertSeverity.HIGH -> SeverityHigh
    com.example.resqai.model.AlertSeverity.MEDIUM -> SeverityMedium
    com.example.resqai.model.AlertSeverity.LOW -> SeverityLow
}

/** Maps [com.example.resqai.model.AlertStatus] to its display color. */
fun statusColor(status: com.example.resqai.model.AlertStatus): Color = when (status) {
    com.example.resqai.model.AlertStatus.ACTIVE -> SeverityHigh
    com.example.resqai.model.AlertStatus.MONITORING -> StatusInfo
    com.example.resqai.model.AlertStatus.RESOLVED -> StatusSafe
}

@Preview(showBackground = true)
@Composable
private fun StatusChipPreview() {
    ResQAITheme {
        StatusChip(text = "HIGH", color = SeverityHigh)
    }
}