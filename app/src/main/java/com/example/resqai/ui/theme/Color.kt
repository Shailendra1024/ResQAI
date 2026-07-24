package com.example.resqai.ui.theme

import androidx.compose.ui.graphics.Color

// Brand colors (from design spec)
val ResQPrimary = Color(0xFFD32F2F)      // Alert red
val ResQSecondary = Color(0xFF1976D2)    // Trust blue
val ResQSuccess = Color(0xFF2E7D32)      // Safe green
val ResQBackgroundLight = Color(0xFFFAFAFA)

// Derived light-theme tones
val ResQPrimaryContainerLight = Color(0xFFFFDAD6)
val ResQSecondaryContainerLight = Color(0xFFD0E4FF)
val ResQSurfaceLight = Color(0xFFFFFFFF)
val ResQOnPrimaryLight = Color(0xFFFFFFFF)
val ResQErrorLight = Color(0xFFB00020)

// Derived dark-theme tones
val ResQBackgroundDark = Color(0xFF121212)
val ResQSurfaceDark = Color(0xFF1E1E1E)
val ResQPrimaryContainerDark = Color(0xFF5C1A1A)
val ResQSecondaryContainerDark = Color(0xFF0D3E70)
val ResQOnSurfaceDark = Color(0xFFECECEC)

// Semantic / status colors used across the app (alerts, chips, badges)
val SeverityHigh = Color(0xFFD32F2F)
val SeverityMedium = Color(0xFFF57C00)
val SeverityLow = Color(0xFFFBC02D)
val StatusSafe = ResQSuccess
val StatusInfo = ResQSecondary