package com.example.resqai.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.resqai.ui.theme.ResQAITheme

/**
 * Reusable confirmation dialog used across the app (SOS confirmation,
 * Logout confirmation, destructive-action confirmations, etc.).
 */
@Composable
fun CustomDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    dismissText: String = "Cancel",
    isDestructive: Boolean = false
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title, style = MaterialTheme.typography.titleMedium) },
        text = { Text(text = message, style = MaterialTheme.typography.bodyMedium) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    color = if (isDestructive) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismissText)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun CustomDialogPreview() {
    ResQAITheme {
        CustomDialog(
            title = "Send SOS Alert?",
            message = "This will immediately notify emergency responders of your location.",
            confirmText = "Send SOS",
            onConfirm = {},
            onDismiss = {},
            isDestructive = true
        )
    }
}