package com.example.resqai.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.resqai.ui.theme.ResQAITheme

/** Bottom nav destinations, in display order. */
enum class BottomNavItem(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Filled.Home),
    Map("Map", Icons.Filled.Map),
    Sos("SOS", Icons.Filled.Sos),
    History("History", Icons.Filled.History),
    Profile("Profile", Icons.Filled.Person)
}

/**
 * App-wide bottom navigation bar shown on Home and its sibling top-level screens.
 * Purely presentational — navigation logic lives in the caller (NavGraph).
 */
@Composable
fun ResQBottomNavigationBar(
    currentItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit
) {
    NavigationBar {
        BottomNavItem.values().forEach { item ->
            NavigationBarItem(
                selected = item == currentItem,
                onClick = { onItemSelected(item) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomNavigationBarPreview() {
    ResQAITheme {
        ResQBottomNavigationBar(currentItem = BottomNavItem.Home, onItemSelected = {})
    }
}