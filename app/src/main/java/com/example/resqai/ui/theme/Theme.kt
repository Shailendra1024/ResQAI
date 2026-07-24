package com.example.resqai.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = ResQPrimary,
    onPrimary = ResQOnPrimaryLight,
    primaryContainer = ResQPrimaryContainerLight,
    secondary = ResQSecondary,
    secondaryContainer = ResQSecondaryContainerLight,
    background = ResQBackgroundLight,
    surface = ResQSurfaceLight,
    error = ResQErrorLight,
)

private val DarkColors = darkColorScheme(
    primary = ResQPrimary,
    onPrimary = ResQOnPrimaryLight,
    primaryContainer = ResQPrimaryContainerDark,
    secondary = ResQSecondary,
    secondaryContainer = ResQSecondaryContainerDark,
    background = ResQBackgroundDark,
    surface = ResQSurfaceDark,
    onSurface = ResQOnSurfaceDark,
    error = ResQErrorLight,
)

/**
 * App-wide Material 3 theme.
 * - Respects system dark mode automatically (isSystemInDarkTheme)
 * - Dynamic color (Android 12+) disabled by default to keep brand colors
 *   consistent; flip [useDynamicColor] to true to allow Material You.
 */
@Composable
fun ResQAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ResQTypography,
        shapes = ResQShapes,
        content = content
    )
}

