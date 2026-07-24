package com.example.resqai.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.resqai.ui.theme.ResQAITheme

/**
 * Standard top app bar used across most screens.
 * @param showBackButton toggles a back arrow (used on non-root screens).
 * @param showNotificationIcon toggles a bell icon (used on Home).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResQTopAppBar(
    title: String,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    showNotificationIcon: Boolean = false,
    onNotificationClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            if (showNotificationIcon) {
                IconButton(onClick = onNotificationClick) {
                    Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
    )
}

@Preview(showBackground = true)
@Composable
private fun ResQTopAppBarPreview() {
    ResQAITheme {
        ResQTopAppBar(title = "Home", showNotificationIcon = true)
    }
}