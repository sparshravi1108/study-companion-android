package com.example.domain.model

data class UserProfile(
    val uid: String = "",
    val displayName: String = "Alex Carter",
    val email: String = "alex@example.com",
    val photoUrl: String? = null,
    val joinedDate: String = "Apr 2026",
    val timezone: String = "Asia/Kolkata",
    val stats: UserStats = UserStats(),
    val preferences: UserPreferences = UserPreferences(),
    val integrations: ConnectedIntegrations = ConnectedIntegrations()
)

data class UserStats(
    val level: Int = 1,
    val levelTitle: String = "Beginner",
    val currentXp: Int = 0,
    val nextLevelXp: Int = 500,
    val streakDays: Int = 0,
    val totalStudyMinutes: Int = 0,
    val focusPercentage: Int = 0
) {
    val formattedStudyTime: String
        get() {
            val hours = totalStudyMinutes / 60
            val mins = totalStudyMinutes % 60
            return "${hours}h ${mins}m"
        }
}

data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.INK_ANIME,
    val animationLevel: AnimationLevel = AnimationLevel.MEDIUM,
    val animationIntensity: Float = 1.0f,
    val reduceMotion: Boolean = false,
    val soundEffectsEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true
)

data class ConnectedIntegrations(
    val googleDriveSync: Boolean = true,
    val googleCalendarSync: Boolean = true,
    val googleTasksSync: Boolean = true,
    val googleKeepSync: Boolean = true,
    val gmailSync: Boolean = true
)
