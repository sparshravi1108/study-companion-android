package com.example.domain.model

enum class ThemeMode(
    val title: String,
    val subtitle: String,
    val badge: String,
    val isDark: Boolean = true
) {
    LIGHT(
        title = "Light",
        subtitle = "Crisp • Clean • High Contrast",
        badge = "Day",
        isDark = false
    ),
    DARK(
        title = "Dark",
        subtitle = "Classic • Deep • Sleek",
        badge = "Night",
        isDark = true
    ),
    INK_ANIME(
        title = "Ink Anime",
        subtitle = "Bold • Artistic • Focused",
        badge = "Signature",
        isDark = true
    ),
    NEON_NIGHT(
        title = "Neon Night",
        subtitle = "Vibrant • Energetic • Modern",
        badge = "Cyber",
        isDark = true
    ),
    LIGHTNING(
        title = "Lightning",
        subtitle = "Dynamic • Powerful • Sharp",
        badge = "Power",
        isDark = true
    ),
    CALM(
        title = "Calm",
        subtitle = "Soothing • Minimal • Clean",
        badge = "Zen",
        isDark = true
    ),
    EXAM(
        title = "Exam",
        subtitle = "Focused • Minimal • Intense",
        badge = "Strict",
        isDark = true
    )
}
