package com.example.resqai.screens.authentication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.resqai.components.PrimaryButton
import com.example.resqai.components.ResQPasswordField
import com.example.resqai.components.ResQTextField
import com.example.resqai.ui.theme.ResQAITheme
import com.example.resqai.viewmodel.AuthViewModel

/**
 * Register screen — calls the real backend (POST /api/v1/auth/register, then login)
 * through [AuthViewModel]. Phone and location are collected in the form but the
 * backend does not store them yet.
 */
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val passwordsMismatch = confirmPassword.isNotEmpty() && password != confirmPassword
    val passwordTooShort = password.isNotEmpty() && password.length < 8

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp)
        )
        Text(
            text = "Join ResQAI to stay safe and informed",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        ResQTextField(
            value = name,
            onValueChange = {
                name = it
                viewModel.clearError()
            },
            label = "Full Name",
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) }
        )

        FieldSpacer()

        ResQTextField(
            value = email,
            onValueChange = {
                email = it
                viewModel.clearError()
            },
            label = "Email",
            keyboardType = KeyboardType.Email,
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) }
        )

        FieldSpacer()

        ResQTextField(
            value = phone,
            onValueChange = { phone = it },
            label = "Phone Number",
            keyboardType = KeyboardType.Phone,
            leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) }
        )

        FieldSpacer()

        ResQTextField(
            value = location,
            onValueChange = { location = it },
            label = "Location",
            leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = null) }
        )

        FieldSpacer()

        ResQPasswordField(
            value = password,
            onValueChange = {
                password = it
                viewModel.clearError()
            },
            label = "Password",
            isError = passwordTooShort,
            errorMessage = "Password must be at least 8 characters"
        )

        FieldSpacer()

        ResQPasswordField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirm Password",
            isError = passwordsMismatch,
            errorMessage = "Passwords do not match"
        )

        // Error from the backend (email already used, server unreachable, etc.)
        error?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
        }

        PrimaryButton(
            text = "Create Account",
            loading = isLoading,
            enabled = name.trim().length >= 2 &&
                    email.isNotBlank() &&
                    password.length >= 8 &&
                    password == confirmPassword &&
                    !isLoading,
            onClick = { viewModel.register(name, email, password, onRegisterSuccess) },
            modifier = Modifier.padding(top = 28.dp)
        )

        Row(
            modifier = Modifier.padding(top = 20.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account?",
                style = MaterialTheme.typography.bodyMedium
            )
            TextButton(onClick = onNavigateToLogin) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/** Small consistent gap between form fields. */
@Composable
private fun FieldSpacer() {
    androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 8.dp))
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    ResQAITheme {
        RegisterScreen(onRegisterSuccess = {}, onNavigateToLogin = {})
    }
}