package com.example.resqai.screens.profile

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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.resqai.components.PrimaryButton
import com.example.resqai.components.ProfileCard
import com.example.resqai.components.ProfileInfoRow
import com.example.resqai.components.ResQTopAppBar
import com.example.resqai.model.User
import com.example.resqai.ui.theme.ResQAITheme
import com.example.resqai.viewmodel.ProfileUiState
import com.example.resqai.viewmodel.ProfileViewModel

/**
 * Profile screen — avatar, name/email/phone, emergency contacts,
 * medical information, and an Edit toggle for basic fields.
 */
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = remember { ProfileViewModel() },
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val isEditing by viewModel.isEditing.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    Scaffold(
        topBar = {
            ResQTopAppBar(
                title = "Profile",
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        when (val profileState = state) {
            is ProfileUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ProfileUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = profileState.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is ProfileUiState.Success -> {
                ProfileContent(
                    user = profileState.user,
                    isEditing = isEditing,
                    isSaving = isSaving,
                    modifier = Modifier.padding(paddingValues),
                    onEditToggle = viewModel::toggleEditMode,
                    onNameChange = { newName ->
                        viewModel.updateField { it.copy(name = newName) }
                    },
                    onPhoneChange = { newPhone ->
                        viewModel.updateField { it.copy(phone = newPhone) }
                    },
                    onSave = viewModel::saveProfile
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    user: User,
    isEditing: Boolean,
    isSaving: Boolean,
    modifier: Modifier = Modifier,
    onEditToggle: () -> Unit,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar placeholder — replace with real image loading later
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(96.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile picture placeholder",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isEditing) {
                OutlinedTextField(
                    value = user.name,
                    onValueChange = onNameChange,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleLarge,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )
            } else {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = onEditToggle) {
                Icon(
                    imageVector = if (isEditing) Icons.Filled.Save else Icons.Filled.Edit,
                    contentDescription = if (isEditing) "Save" else "Edit profile",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        ProfileCard(
            title = "Contact Information",
            modifier = Modifier.padding(top = 24.dp)
        ) {
            ProfileInfoRow(label = "Email", value = user.email)
            if (isEditing) {
                OutlinedTextField(
                    value = user.phone,
                    onValueChange = onPhoneChange,
                    label = { Text("Phone") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )
            } else {
                ProfileInfoRow(label = "Phone", value = user.phone)
            }
        }

        ProfileCard(
            title = "Emergency Contacts",
            modifier = Modifier.padding(top = 16.dp)
        ) {
            if (user.emergencyContacts.isEmpty()) {
                Text(
                    text = "No emergency contacts added yet",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                user.emergencyContacts.forEach { contact ->
                    ProfileInfoRow(
                        label = "${contact.name} (${contact.relation})",
                        value = contact.phoneNumber
                    )
                }
            }
        }

        ProfileCard(
            title = "Medical Information",
            modifier = Modifier.padding(top = 16.dp)
        ) {
            ProfileInfoRow(label = "Blood Group", value = user.bloodGroup)
            ProfileInfoRow(label = "Allergies", value = user.allergies)
            ProfileInfoRow(label = "Conditions", value = user.medicalConditions)
        }

        if (isEditing) {
            PrimaryButton(
                text = "Save Changes",
                loading = isSaving,
                onClick = onSave,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = remember { ProfileViewModel() },
    onBackClick: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    // ... existing state collection ...

    Scaffold(
        topBar = {
            ResQTopAppBar(
                title = "Profile",
                showBackButton = true,
                onBackClick = onBackClick,
                showNotificationIcon = true, // reused as a settings-icon slot
                onNotificationClick = onNavigateToSettings
            )
        }
    ) { paddingValues ->
        // ... unchanged ...
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    ResQAITheme {
        ProfileScreen(onBackClick = {})
    }
}