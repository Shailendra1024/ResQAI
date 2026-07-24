package com.example.resqai.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.resqai.screens.alert.AlertDetailsScreen
import com.example.resqai.screens.alert.AlertListScreen
import com.example.resqai.screens.authentication.LoginScreen
import com.example.resqai.screens.authentication.RegisterScreen
import com.example.resqai.screens.history.HistoryScreen
import com.example.resqai.screens.home.HomeScreen
import com.example.resqai.screens.map.SafeRouteScreen
import com.example.resqai.screens.onboarding.OnboardingScreen
import com.example.resqai.screens.profile.ProfileScreen
import com.example.resqai.screens.settings.SettingsScreen
import com.example.resqai.screens.shelter.ShelterScreen
import com.example.resqai.screens.sos.RescueStatusScreen
import com.example.resqai.screens.sos.SosScreen
import com.example.resqai.screens.splash.SplashScreen
import com.example.resqai.viewmodel.SOSViewModel

@Composable
fun ResQNavGraph(
    navController: NavHostController = rememberNavController(),
    onDarkModeChanged: (Boolean) -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(
            route = Screen.Splash.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Onboarding.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Login.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(
            route = Screen.Register.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Home.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            HomeScreen(
                onNavigateToAlerts = { navController.navigate(Screen.Alerts.route) },
                onNavigateToSafeRoute = { navController.navigate(Screen.SafeRoute.route) },
                onNavigateToShelters = { navController.navigate(Screen.Shelters.route) },
                onNavigateToSos = { navController.navigate(Screen.Sos.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
            )
        }

        composable(
            route = Screen.Alerts.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            AlertListScreen(
                onBackClick = { navController.popBackStack() },
                onAlertClick = { alertId ->
                    navController.navigate(Screen.AlertDetails.createRoute(alertId))
                }
            )
        }

        composable(
            route = Screen.AlertDetails.route,
            arguments = listOf(navArgument("alertId") { type = NavType.StringType }),
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { backStackEntry ->
            val alertId = backStackEntry.arguments?.getString("alertId") ?: ""
            AlertDetailsScreen(
                alertId = alertId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.SafeRoute.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            SafeRouteScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Shelters.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            ShelterScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Sos.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            val sosViewModel: SOSViewModel = viewModel()
            SosScreen(
                viewModel = sosViewModel,
                onBackClick = { navController.popBackStack() },
                onViewRescueStatus = { navController.navigate(Screen.RescueStatus.route) }
            )
        }

        composable(
            route = Screen.RescueStatus.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Sos.route)
            }
            val sosViewModel: SOSViewModel = viewModel(parentEntry)
            RescueStatusScreen(
                viewModel = sosViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.History.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            HistoryScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Profile.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(
            route = Screen.Settings.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            SettingsScreen(
                onDarkModeChanged = onDarkModeChanged,
                onBackClick = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
    }
}