package com.example.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Main : Screen("main")
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
}

sealed class BottomNavTab(
    val route: String,
    val title: String
) {
    data object Home : BottomNavTab("home_tab", "Home")
    data object Subjects : BottomNavTab("subjects_tab", "Subjects")
    data object Roadmap : BottomNavTab("roadmap_tab", "Roadmap")
    data object More : BottomNavTab("more_tab", "More")
    data object Profile : BottomNavTab("profile_tab", "Profile")
    data object Settings : BottomNavTab("settings_tab", "Settings")
}
