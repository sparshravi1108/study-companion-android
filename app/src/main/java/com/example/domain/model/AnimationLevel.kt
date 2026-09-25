package com.example.domain.model

enum class AnimationLevel(
    val label: String,
    val description: String,
    val multiplier: Float,
    val particleDensity: Float,
    val glowIntensity: Float
) {
    LOW(
        label = "Low",
        description = "Subtle motion, reduced glow",
        multiplier = 0.6f,
        particleDensity = 0.3f,
        glowIntensity = 0.5f
    ),
    MEDIUM(
        label = "Medium",
        description = "Default smooth balanced motion",
        multiplier = 1.0f,
        particleDensity = 0.7f,
        glowIntensity = 0.8f
    ),
    HIGH(
        label = "High",
        description = "Energetic & dynamic effects",
        multiplier = 1.25f,
        particleDensity = 1.0f,
        glowIntensity = 1.0f
    ),
    EXTREME(
        label = "Extreme",
        description = "Cinematic energy & intense motion",
        multiplier = 1.5f,
        particleDensity = 1.4f,
        glowIntensity = 1.2f
    )
}
