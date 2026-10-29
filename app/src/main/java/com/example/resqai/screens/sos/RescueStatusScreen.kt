package com.example.resqai.screens.sos

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resqai.components.ResQTopAppBar
import com.example.resqai.model.SOSStage
import com.example.resqai.ui.theme.ResQAITheme
import com.example.resqai.viewmodel.SOSViewModel

/**
 * Request Status screen — animated vertical timeline:
 * Sent → Responder Assigned → En Route → Completed.
 *
 * IMPORTANT: the stages after "Sent" are SIMULATED by the app for demonstration
 * ([SOSViewModel.simulateRescueProgress]). No real responder is involved, and the
 * screen says so.
 */
@Composable
fun RescueStatusScreen(
    viewModel: SOSViewModel,
    onBackClick: () -> Unit
) {
    val currentStage by viewModel.currentStage.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.simulateRescueProgress()
    }

    Scaffold(
        topBar = {
            ResQTopAppBar(
                title = "Request Status (Demo)",
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            DemoNoticeCard(onCall112 = { dialNumber(context, "112") })

            Column(modifier = Modifier.padding(top = 24.dp)) {
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
}

/** Makes it clear that the progress below is a simulation, not a real rescue. */
@Composable
private fun DemoNoticeCard(onCall112: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Demo simulation",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Text(
                text = "The stages below are simulated by the app. No real responder has " +
                        "been assigned and no help is on the way. In a real emergency, " +
                        "call 112.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(top = 8.dp)
            )
            Button(
                onClick = onCall112,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text(text = "Call 112", modifier = Modifier.padding(start = 8.dp))
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
    val labels = listOf(
        "SOS Recorded",
        "Responder Assigned (simulated)",
        "Responder En Route (simulated)",
        "Completed (simulated)"
    )
    val currentIndex = order.indexOf(currentStage).coerceAtLeast(0)

    return labels.mapIndexed { index, label ->
        Triple(
            label,
            index < currentIndex || (index == currentIndex && currentStage == SOSStage.RESCUE_COMPLETED),
            index == currentIndex && currentStage != SOSStage.RESCUE_COMPLETED
        )
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