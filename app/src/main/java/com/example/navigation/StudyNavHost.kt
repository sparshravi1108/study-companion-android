package com.example.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.core.network.NetworkMonitor
import com.example.domain.dashboard.model.DashboardData
import com.example.domain.dashboard.repository.DashboardRepository
import com.example.domain.model.AnimationLevel
import com.example.domain.model.ThemeMode
import com.example.domain.model.UserProfile
import com.example.domain.repository.UserRepository
import com.example.presentation.auth.AuthViewModel
import com.example.presentation.auth.LoginScreen
import com.example.presentation.dashboard.DashboardUiState
import com.example.presentation.dashboard.DashboardViewModel
import com.example.presentation.dashboard.customization.DashboardCustomizationScreen
import com.example.presentation.main.MainScreen
import com.example.presentation.profile.ProfileScreen
import com.example.presentation.settings.SettingsScreen
import com.example.presentation.splash.SplashScreen
import com.example.ui.animation.StudyTransitions
import com.example.ui.animation.TransitionPreset
import com.example.ui.theme.StudyTheme

@Composable
fun StudyNavHost(
    authViewModel: AuthViewModel,
    userRepository: UserRepository,
    dashboardRepository: DashboardRepository,
    networkMonitor: NetworkMonitor,
    currentUserProfile: UserProfile,
    onAppearanceChanged: (ThemeMode, AnimationLevel, Float, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val isOnline by networkMonitor.isOnline.collectAsStateWithLifecycle(
        initialValue = networkMonitor.isCurrentlyOnline()
    )
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()

    val dashboardViewModel: DashboardViewModel = viewModel {
        DashboardViewModel(dashboardRepository, networkMonitor)
    }

    val animConfig = StudyTheme.animationConfig
    val currentTheme = currentUserProfile.preferences.themeMode
    val transitionPreset = when {
        animConfig.reduceMotion -> TransitionPreset.REDUCE_MOTION
        currentTheme == ThemeMode.INK_ANIME -> TransitionPreset.INK
        currentTheme == ThemeMode.LIGHTNING -> TransitionPreset.LIGHTNING
        currentTheme == ThemeMode.CALM -> TransitionPreset.CALM
        currentTheme == ThemeMode.EXAM -> TransitionPreset.EXAM
        currentTheme == ThemeMode.NEON_NIGHT -> TransitionPreset.CINEMATIC
        else -> TransitionPreset.STANDARD
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(
            route = Screen.Splash.route,
            enterTransition = { StudyTransitions.getEnterTransition(transitionPreset, this, animConfig) },
            exitTransition = { StudyTransitions.getExitTransition(transitionPreset, this, animConfig) }
        ) {
            SplashScreen(
                onSplashComplete = {
                    if (currentUser != null) {
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(
            route = Screen.Login.route,
            enterTransition = { StudyTransitions.getEnterTransition(transitionPreset, this, animConfig) },
            exitTransition = { StudyTransitions.getExitTransition(transitionPreset, this, animConfig) }
        ) {
            LoginScreen(
                viewModel = authViewModel,
                onAuthSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Main.route,
            enterTransition = { StudyTransitions.getEnterTransition(transitionPreset, this, animConfig) },
            exitTransition = { StudyTransitions.getExitTransition(transitionPreset, this, animConfig) }
        ) {
            MainScreen(
                userProfile = currentUserProfile,
                dashboardViewModel = dashboardViewModel,
                isOnline = isOnline,
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToCustomization = {
                    navController.navigate(Screen.DashboardCustomization.route)
                }
            )
        }

        composable(
            route = Screen.DashboardCustomization.route,
            enterTransition = { StudyTransitions.getEnterTransition(transitionPreset, this, animConfig) },
            exitTransition = { StudyTransitions.getExitTransition(transitionPreset, this, animConfig) }
        ) {
            val uiState by dashboardViewModel.uiState.collectAsStateWithLifecycle()
            val configs = (uiState as? DashboardUiState.Content)?.data?.widgetConfigs
                ?: DashboardData.defaultWidgetConfigs()

            DashboardCustomizationScreen(
                currentConfigs = configs,
                onBackClick = { navController.popBackStack() },
                onSaveConfigs = { newConfigs ->
                    dashboardViewModel.saveCustomization(newConfigs)
                }
            )
        }

        composable(
            route = Screen.Profile.route,
            enterTransition = { StudyTransitions.getEnterTransition(transitionPreset, this, animConfig) },
            exitTransition = { StudyTransitions.getExitTransition(transitionPreset, this, animConfig) }
        ) {
            ProfileScreen(
                userProfile = currentUserProfile,
                onBackClick = { navController.popBackStack() },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Settings.route,
            enterTransition = { StudyTransitions.getEnterTransition(transitionPreset, this, animConfig) },
            exitTransition = { StudyTransitions.getExitTransition(transitionPreset, this, animConfig) }
        ) {
            SettingsScreen(
                userProfile = currentUserProfile,
                onBackClick = { navController.popBackStack() },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onAppearanceChanged = onAppearanceChanged,
                onNavigateToCustomization = {
                    navController.navigate(Screen.DashboardCustomization.route)
                }
            )
        }
    }
}
