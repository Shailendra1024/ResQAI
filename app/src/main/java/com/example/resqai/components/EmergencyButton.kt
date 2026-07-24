package com.example.resqai.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.resqai.ui.theme.ResQAITheme

/**
 * Large circular emergency action button — the core SOS trigger.
 * Extracted as a reusable component so it can be reused (e.g. a smaller
 * version could appear as a floating action button on other screens later).
 */
@Composable
fun EmergencyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 180.dp,
    label: String = "SOS"
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.error,
        modifier = modifier.size(size)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Filled.Sos,
                    contentDescription = label,
                    tint = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.size(size / 3.2f)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmergencyButtonPreview() {
    ResQAITheme {
        EmergencyButton(onClick = {})
    }
}