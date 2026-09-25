package com.example.ui.animation

import androidx.compose.animation.core.Spring
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.domain.model.AnimationLevel

@Immutable
data class AnimationConfig(
    val level: AnimationLevel = AnimationLevel.MEDIUM,
    val reduceMotion: Boolean = false,
    val scaleMultiplier: Float = 1.0f,
    val glowIntensity: Float = 0.8f,
    val particleDensity: Float = 0.7f,
    val springStiffness: Float = Spring.StiffnessMedium,
    val springDamping: Float = Spring.DampingRatioLowBouncy,
    val transitionDurationMs: Int = 300,
    val enableParticles: Boolean = true,
    val enableContinuousAnimations: Boolean = true,
    val enableParallax: Boolean = true
) {
    fun duration(baseMs: Int): Int {
        if (reduceMotion) return (baseMs * 0.4f).toInt().coerceAtLeast(100)
        return (baseMs / scaleMultiplier).toInt().coerceAtLeast(100)
    }
}

val LocalAnimationConfig = staticCompositionLocalOf {
    AnimationConfig()
}

fun createAnimationConfig(
    level: AnimationLevel,
    reduceMotion: Boolean,
    customMultiplier: Float = 1.0f
): AnimationConfig {
    if (reduceMotion) {
        return AnimationConfig(
            level = level,
            reduceMotion = true,
            scaleMultiplier = 0.5f,
            glowIntensity = 0.3f,
            particleDensity = 0.0f,
            springStiffness = Spring.StiffnessHigh,
            springDamping = Spring.DampingRatioNoBouncy,
            transitionDurationMs = 150,
            enableParticles = false,
            enableContinuousAnimations = false,
            enableParallax = false
        )
    }

    val combinedMultiplier = (level.multiplier * customMultiplier).coerceIn(0.5f, 2.0f)
    return when (level) {
        AnimationLevel.LOW -> AnimationConfig(
            level = level,
            reduceMotion = false,
            scaleMultiplier = combinedMultiplier,
            glowIntensity = 0.5f,
            particleDensity = 0.3f,
            springStiffness = Spring.StiffnessMedium,
            springDamping = Spring.DampingRatioNoBouncy,
            transitionDurationMs = 250,
            enableParticles = false,
            enableContinuousAnimations = false,
            enableParallax = false
        )
        AnimationLevel.MEDIUM -> AnimationConfig(
            level = level,
            reduceMotion = false,
            scaleMultiplier = combinedMultiplier,
            glowIntensity = 0.8f,
            particleDensity = 0.7f,
            springStiffness = Spring.StiffnessMediumLow,
            springDamping = Spring.DampingRatioLowBouncy,
            transitionDurationMs = 300,
            enableParticles = true,
            enableContinuousAnimations = true,
            enableParallax = true
        )
        AnimationLevel.HIGH -> AnimationConfig(
            level = level,
            reduceMotion = false,
            scaleMultiplier = combinedMultiplier,
            glowIntensity = 1.0f,
            particleDensity = 1.0f,
            springStiffness = Spring.StiffnessLow,
            springDamping = Spring.DampingRatioMediumBouncy,
            transitionDurationMs = 350,
            enableParticles = true,
            enableContinuousAnimations = true,
            enableParallax = true
        )
        AnimationLevel.EXTREME -> AnimationConfig(
            level = level,
            reduceMotion = false,
            scaleMultiplier = combinedMultiplier,
            glowIntensity = 1.25f,
            particleDensity = 1.3f,
            springStiffness = Spring.StiffnessVeryLow,
            springDamping = Spring.DampingRatioMediumBouncy,
            transitionDurationMs = 400,
            enableParticles = true,
            enableContinuousAnimations = true,
            enableParallax = true
        )
    }
}
