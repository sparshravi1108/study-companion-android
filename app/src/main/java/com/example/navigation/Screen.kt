package com.example.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Main : Screen("main")
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
    data object DashboardCustomization : Screen("dashboard_customization")
}

sealed class BottomNavTab(
    val route: String,
    val title: String
) {
    data object Home : BottomNavTab("home_tab", "Home")
    data object Profile : BottomNavTab("profile_tab", "Profile")
    data object Settings : BottomNavTab("settings_tab", "Settings")
}
