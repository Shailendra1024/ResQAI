package com.example.resqai.screens.sos

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resqai.components.CustomDialog
import com.example.resqai.components.EmergencyButton
import com.example.resqai.components.LoadingAnimation
import com.example.resqai.components.PrimaryButton
import com.example.resqai.components.ResQTopAppBar
import com.example.resqai.model.EmergencyContact
import com.example.resqai.ui.theme.ResQAITheme
import com.example.resqai.viewmodel.SOSViewModel
import com.example.resqai.viewmodel.SosUiState

/** Opens the phone dialer with [number] filled in. No CALL_PHONE permission needed. */
internal fun dialNumber(context: Context, number: String) {
    try {
        context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))
    } catch (e: Exception) {
        // No dialer app available (some emulators) — nothing else to do.
    }
}

/**
 * SOS screen. The SOS button records a request on the ResQAI server, but it does NOT
 * contact any real emergency service — the screen says so clearly and offers a
 * one-tap call to 112.
 */
@Composable
fun SosScreen(
    viewModel: SOSViewModel = remember { SOSViewModel() },
    onBackClick: () -> Unit,
    onViewRescueStatus: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

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
            EmergencyNoticeCard(onCall112 = { dialNumber(context, "112") })

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
                            onViewStatus = onViewRescueStatus,
                            onSendAnother = viewModel::reset,
                            onCall112 = { dialNumber(context, "112") }
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
                text = "Emergency Contacts (tap to call)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 12.dp)
            )
            EmergencyContactCard(
                contact = EmergencyContact(
                    id = "c0",
                    name = "National Emergency Number",
                    phoneNumber = "112"
                ),
                onCall = { dialNumber(context, "112") }
            )
            viewModel.emergencyContacts.forEach { contact ->
                EmergencyContactCard(
                    contact = contact,
                    onCall = { dialNumber(context, contact.phoneNumber) }
                )
            }
        }

        if (state is SosUiState.ConfirmDialogVisible) {
            CustomDialog(
                title = "Send SOS Request?",
                message = "This saves an SOS request with your location on the ResQAI server. " +
                        "It does NOT contact police, ambulance or any rescue service. " +
                        "In a real emergency, call 112 first.",
                confirmText = "Send SOS",
                isDestructive = true,
                onConfirm = viewModel::onConfirmSos,
                onDismiss = viewModel::onDismissDialog
            )
        }
    }
}

/** Always-visible notice at the top: this app does not replace emergency services. */
@Composable
private fun EmergencyNoticeCard(onCall112: () -> Unit) {
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
                    text = "Not a replacement for emergency services",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Text(
                text = "ResQAI is a demo app. SOS requests are not sent to police, " +
                        "ambulance or fire services. In a real emergency, call 112 right away.",
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

@Composable
private fun SosIdleContent(onSosPressed: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 40.dp)
    ) {
        Text(
            text = "Press the button below to record an SOS request",
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
    onViewStatus: () -> Unit,
    onSendAnother: () -> Unit,
    onCall112: () -> Unit
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
            text = "SOS Request Recorded",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Your request was saved on the ResQAI server. No emergency service " +
                    "has been contacted. If you need help, call 112 now.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )
        Button(
            onClick = onCall112,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ),
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Phone,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Text(text = "Call 112", modifier = Modifier.padding(start = 8.dp))
        }
        PrimaryButton(
            text = "View Request Status (Demo)",
            onClick = onViewStatus,
            modifier = Modifier.padding(top = 12.dp)
        )
        androidx.compose.material3.TextButton(onClick = onSendAnother) {
            Text("Send Another SOS")
        }
    }
}

@Composable
private fun EmergencyContactCard(contact: EmergencyContact, onCall: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onCall() },
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