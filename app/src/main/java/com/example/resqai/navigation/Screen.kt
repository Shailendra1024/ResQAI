package com.example.resqai.navigation

/**
 * Single source of truth for all navigation routes in ResQAI.
 * Using a sealed class avoids stringly-typed route bugs.
 */
sealed class Screen(val route: String) {
    object Splash : Screen("splash")

    object Onboarding : Screen("onboarding")

    object Login : Screen("login")
    object Register : Screen("register")

    object Home : Screen("home")

    object Alerts : Screen("alerts")
    object AlertDetails : Screen("alert_details/{alertId}") {
        fun createRoute(alertId: String) = "alert_details/$alertId"
    }

    object SafeRoute : Screen("safe_route")
    object Shelters : Screen("shelters")

    object Sos : Screen("sos")
    object RescueStatus : Screen("rescue_status")

    object History : Screen("history")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}