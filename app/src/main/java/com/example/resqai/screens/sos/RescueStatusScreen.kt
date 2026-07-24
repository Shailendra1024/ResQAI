package com.example.resqai.screens.sos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resqai.components.ResQTopAppBar
import com.example.resqai.model.SOSStage
import com.example.resqai.ui.theme.ResQAITheme
import com.example.resqai.viewmodel.SOSViewModel
import androidx.compose.material3.Icon

/**
 * Rescue Status screen — animated vertical timeline showing SOS progress:
 * Sent → Responder Assigned → En Route → Rescue Completed.
 * Auto-advances via [SOSViewModel.simulateRescueProgress] for demo purposes.
 */
@Composable
fun RescueStatusScreen(
    viewModel: SOSViewModel,
    onBackClick: () -> Unit
) {
    val currentStage by viewModel.currentStage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.simulateRescueProgress()
    }

    Scaffold(
        topBar = {
            ResQTopAppBar(
                title = "Rescue Status",
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            val steps = buildTimelineSteps(currentStage)
            steps.forEachIndexed { index, step ->
                TimelineRow(
                    label = step.first,
                    isCompleted = step.second,
                    isCurrent = step.third,
                    isLast = index == steps.lastIndex
                )
            }
        }
    }
}

/** Builds (label, isCompleted, isCurrent) triples for each timeline step based on current stage. */
private fun buildTimelineSteps(currentStage: SOSStage): List<Triple<String, Boolean, Boolean>> {
    val order = listOf(
        SOSStage.SENT,
        SOSStage.RESPONDER_ASSIGNED,
        SOSStage.RESPONDER_EN_ROUTE,
        SOSStage.RESCUE_COMPLETED
    )
    val labels = listOf("SOS Sent", "Responder Assigned", "Responder En Route", "Rescue Completed")
    val currentIndex = order.indexOf(currentStage).coerceAtLeast(0)

    return labels.mapIndexed { index, label ->
        Triple(label, index < currentIndex || (index == currentIndex && currentStage == SOSStage.RESCUE_COMPLETED), index == currentIndex && currentStage != SOSStage.RESCUE_COMPLETED)
    }
}

@Composable
private fun TimelineRow(
    label: String,
    isCompleted: Boolean,
    isCurrent: Boolean,
    isLast: Boolean
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TimelineNode(isCompleted = isCompleted, isCurrent = isCurrent)
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(48.dp)
                        .background(
                            if (isCompleted) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outlineVariant
                        )
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(start = 16.dp, bottom = if (isLast) 0.dp else 32.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = if (isCurrent || isCompleted) FontWeight.Bold else FontWeight.Normal,
                color = if (isCompleted || isCurrent) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            if (isCurrent) {
                Text(
                    text = "In progress...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (isCompleted) {
                Text(
                    text = "Completed",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TimelineNode(isCompleted: Boolean, isCurrent: Boolean) {
    val color: Color = when {
        isCompleted -> MaterialTheme.colorScheme.primary
        isCurrent -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    Box(
        modifier = Modifier
            .size(28.dp)
            .background(color.copy(alpha = if (isCompleted || isCurrent) 1f else 0.3f), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        when {
            isCompleted -> Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(16.dp)
            )
            isCurrent -> CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RescueStatusScreenPreview() {
    ResQAITheme {
        RescueStatusScreen(viewModel = remember { SOSViewModel() }, onBackClick = {})
    }
}