package com.example.resqai.screens.sos

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resqai.components.CustomDialog
import com.example.resqai.components.LoadingAnimation
import com.example.resqai.components.PrimaryButton
import com.example.resqai.components.ResQTopAppBar
import com.example.resqai.model.EmergencyContact
import com.example.resqai.ui.theme.ResQAITheme
import com.example.resqai.viewmodel.SOSViewModel
import com.example.resqai.viewmodel.SosUiState
import com.example.resqai.components.EmergencyButton
/**
 * SOS screen — large emergency button that, when confirmed, simulates
 * sending an emergency request with a progress animation and success state.
 */
@Composable
fun SosScreen(
    viewModel: SOSViewModel = remember { SOSViewModel() },
    onBackClick: () -> Unit,
    onViewRescueStatus: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ResQTopAppBar(
                title = "Emergency SOS",
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(targetState = state, label = "sosStateAnim") { sosState ->
                when (sosState) {
                    is SosUiState.Idle, is SosUiState.ConfirmDialogVisible -> {
                        SosIdleContent(onSosPressed = viewModel::onSosButtonPressed)
                    }
                    is SosUiState.Sending -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingAnimation(label = "Sending your SOS request...")
                        }
                    }
                    is SosUiState.Sent -> {
                        SosSuccessContent(
                            responderName = sosState.request.responderName,
                            responderEta = sosState.request.responderEta,
                            onViewStatus = onViewRescueStatus,
                            onSendAnother = viewModel::reset
                        )
                    }
                    is SosUiState.Error -> {
                        Text(
                            text = "Error: ${sosState.message}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(vertical = 40.dp)
                        )
                    }
                }
            }

            Text(
                text = "Emergency Contacts",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 12.dp)
            )
            viewModel.emergencyContacts.forEach { contact ->
                EmergencyContactCard(contact)
            }
        }

        if (state is SosUiState.ConfirmDialogVisible) {
            CustomDialog(
                title = "Send SOS Alert?",
                message = "This will immediately notify emergency responders of your current location. Only use this in a genuine emergency.",
                confirmText = "Send SOS",
                isDestructive = true,
                onConfirm = viewModel::onConfirmSos,
                onDismiss = viewModel::onDismissDialog
            )
        }
    }
}

@Composable
private fun SosIdleContent(onSosPressed: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 40.dp)
    ) {
        Text(
            text = "Press the button below in case of emergency",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Surface(
            onClick = onSosPressed,
            shape = CircleShape,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(180.dp)
        ) {
            EmergencyButton(onClick = onSosPressed)
            Box(contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.Sos,
                        contentDescription = "SOS",
                        tint = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.size(56.dp)
                    )
                    Text(
                        text = "SOS",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onError
                    )
                }
            }
        }
    }
}

@Composable
private fun SosSuccessContent(
    responderName: String,
    responderEta: String,
    onViewStatus: () -> Unit,
    onSendAnother: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(72.dp)
        )
        Text(
            text = "SOS Request Sent!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "$responderName has been notified. Estimated arrival: $responderEta",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )
        PrimaryButton(
            text = "View Rescue Status",
            onClick = onViewStatus,
            modifier = Modifier.padding(top = 24.dp)
        )
        androidx.compose.material3.TextButton(onClick = onSendAnother) {
            Text("Send Another SOS")
        }
    }
}

@Composable
private fun EmergencyContactCard(contact: EmergencyContact) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
            Text(
                text = contact.phoneNumber,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SosScreenPreview() {
    ResQAITheme {
        SosScreen(onBackClick = {}, onViewRescueStatus = {})
    }
}