package com.example.resqai.screens.splash

import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resqai.ui.theme.ResQAITheme
import kotlinx.coroutines.delay

/**
 * First screen shown on app launch.
 * Displays logo + app name + loading animation, then auto-navigates
 * to Onboarding after [SPLASH_DELAY_MS].
 */
private const val SPLASH_DELAY_MS = 2000L

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    // Simple scale-in animation for the logo mark
    var startAnimation by remember { mutableStateOf(false) }
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 600, easing = EaseOutBack),
        label = "logoScale"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(SPLASH_DELAY_MS)
        onSplashFinished()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingValues(24.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo placeholder — replace with actual app icon/illustration later
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .let { it }, // scale applied to inner content
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .size(100.dp),
                    shape = CircleShape,
                    color = Color.White,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Shield,
                            contentDescription = "ResQAI Logo",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(56.dp)
                                .padding(4.dp)
                        )
                    }
                }
            }

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(12.dp))

            Text(
                text = "ResQAI",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "AI-Powered Disaster Alert & Response",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f)
            )

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(20.dp))

            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 3.dp,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    ResQAITheme {
        SplashScreen(onSplashFinished = {})
    }
}