package com.example.resqai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.resqai.navigation.ResQNavGraph
import com.example.resqai.ui.theme.ResQAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResQAIApp()
        }
    }
}

/**
 * Root composable. Holds app-wide dark mode state so the Settings screen's
 * toggle can flip the entire app's theme (not just its own screen).
 * Defaults to the system theme setting until the user overrides it.
 */
@Composable
fun ResQAIApp() {
    var isDarkMode by remember { mutableStateOf<Boolean?>(null) }

    ResQAITheme(
        darkTheme = isDarkMode ?: androidx.compose.foundation.isSystemInDarkTheme()
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            ResQNavGraph(
                onDarkModeChanged = { isDarkMode = it }
            )
        }
    }
}