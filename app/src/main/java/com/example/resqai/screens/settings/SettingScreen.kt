package com.example.resqai.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resqai.components.CustomDialog
import com.example.resqai.components.ResQTopAppBar
import com.example.resqai.ui.theme.ResQAITheme
import com.example.resqai.viewmodel.SettingsViewModel
import com.example.resqai.viewmodel.supportedLanguages
import androidx.compose.foundation.clickable

/**
 * Settings screen — Dark Mode, Notifications, Language, Location Permission,
 * About App, and Logout (with confirmation dialog).
 *
 * @param onDarkModeChanged bubbles the dark-mode toggle up to [com.example.resqai.MainActivity]
 * so the whole app's theme updates immediately, not just this screen.
 */
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = remember { SettingsViewModel() },
    onDarkModeChanged: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onLogout: () -> Unit
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val locationGranted by viewModel.locationPermissionGranted.collectAsState()

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ResQTopAppBar(
                title = "Settings",
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
                .padding(16.dp)
        ) {
            SettingsSectionCard {
                SettingsToggleRow(
                    icon = Icons.Filled.DarkMode,
                    title = "Dark Mode",
                    subtitle = "Switch between light and dark theme",
                    checked = isDarkMode,
                    onCheckedChange = {
                        viewModel.toggleDarkMode()
                        onDarkModeChanged(it)
                    }
                )
                SettingsDivider()
                SettingsToggleRow(
                    icon = Icons.Filled.Notifications,
                    title = "Notifications",
                    subtitle = "Receive disaster alerts and updates",
                    checked = notificationsEnabled,
                    onCheckedChange = { viewModel.toggleNotifications() }
                )
                SettingsDivider()
                SettingsToggleRow(
                    icon = Icons.Filled.LocationOn,
                    title = "Location Permission",
                    subtitle = if (locationGranted) "Granted" else "Not granted",
                    checked = locationGranted,
                    onCheckedChange = { viewModel.toggleLocationPermission() }
                )
            }

            SettingsSectionCard(modifier = Modifier.padding(top = 16.dp)) {
                SettingsNavigationRow(
                    icon = Icons.Filled.Language,
                    title = "Language",
                    subtitle = selectedLanguage,
                    onClick = { showLanguageDialog = true }
                )
                SettingsDivider()
                SettingsNavigationRow(
                    icon = Icons.Filled.Info,
                    title = "About App",
                    subtitle = "ResQAI v1.0",
                    onClick = { showAboutDialog = true }
                )
            }

            SettingsSectionCard(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)) {
                SettingsNavigationRow(
                    icon = Icons.Filled.Logout,
                    title = "Logout",
                    subtitle = "Sign out of your account",
                    titleColor = MaterialTheme.colorScheme.error,
                    onClick = { showLogoutDialog = true }
                )
            }
        }

        if (showLanguageDialog) {
            LanguagePickerDialog(
                selectedLanguage = selectedLanguage,
                onLanguageSelected = {
                    viewModel.onLanguageSelected(it)
                    showLanguageDialog = false
                },
                onDismiss = { showLanguageDialog = false }
            )
        }

        if (showAboutDialog) {
            AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                title = { Text("About ResQAI") },
                text = {
                    Text(
                        "ResQAI is an AI-powered disaster alert and emergency response " +
                                "app that helps you stay informed, find safe evacuation routes, " +
                                "and reach emergency services quickly.\n\nVersion 1.0"
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showAboutDialog = false }) { Text("Close") }
                }
            )
        }

        if (showLogoutDialog) {
            CustomDialog(
                title = "Log Out?",
                message = "Are you sure you want to log out of ResQAI?",
                confirmText = "Logout",
                isDestructive = true,
                onConfirm = {
                    showLogoutDialog = false
                    onLogout()
                },
                onDismiss = { showLogoutDialog = false }
            )
        }
    }
}

@Composable
private fun SettingsSectionCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            content()
        }
    }
}

@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
private fun SettingsNavigationRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    titleColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(imageVector = icon, contentDescription = null, tint = titleColor)
            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, color = titleColor)
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SettingsDivider() {
    androidx.compose.material3.HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.outlineVariant
    )
}

@Composable
private fun LanguagePickerDialog(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Language") },
        text = {
            Column {
                supportedLanguages.forEach { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = language == selectedLanguage,
                            onClick = { onLanguageSelected(language) }
                        )
                        Text(text = language, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Done") }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    ResQAITheme {
        SettingsScreen(onDarkModeChanged = {}, onBackClick = {}, onLogout = {})
    }
}