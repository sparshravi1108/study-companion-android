package com.example.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.animation.ParticleEffect
import com.example.ui.animation.ParticlePreset
import com.example.ui.theme.StudyTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun InkBackground(
    modifier: Modifier = Modifier,
    showParticles: Boolean = true,
    particlePreset: ParticlePreset = ParticlePreset.INK,
    content: @Composable BoxScope.() -> Unit
) {
    val extendedColors = StudyTheme.extendedColors
    val config = StudyTheme.animationConfig

    val infiniteTransition = rememberInfiniteTransition(label = "InkBackgroundTransition")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28318f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = config.duration(12000), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "InkWaveOffset"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Deep background gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(extendedColors.backgroundGradient)
        )

        // Subtle organic fluid ink shapes (rendered on Canvas with low alpha)
        if (config.enableContinuousAnimations && !config.reduceMotion) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                // Top right ink cloud
                val cx1 = width * 0.85f + cos(waveOffset) * 20f
                val cy1 = height * 0.15f + sin(waveOffset) * 15f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            extendedColors.glowSoft.copy(alpha = 0.08f * config.glowIntensity),
                            extendedColors.primary.copy(alpha = 0.03f * config.glowIntensity),
                            Color.Transparent
                        ),
                        center = Offset(cx1, cy1),
                        radius = width * 0.65f
                    ),
                    center = Offset(cx1, cy1),
                    radius = width * 0.65f
                )

                // Bottom left ink cloud
                val cx2 = width * 0.15f + sin(waveOffset) * 20f
                val cy2 = height * 0.75f + cos(waveOffset) * 25f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            extendedColors.secondary.copy(alpha = 0.06f * config.glowIntensity),
                            extendedColors.glowAi.copy(alpha = 0.02f * config.glowIntensity),
                            Color.Transparent
                        ),
                        center = Offset(cx2, cy2),
                        radius = width * 0.75f
                    ),
                    center = Offset(cx2, cy2),
                    radius = width * 0.75f
                )
            }
        }

        // Floating ambient particles (subtle)
        if (showParticles && config.enableParticles && !config.reduceMotion) {
            ParticleEffect(
                preset = particlePreset,
                isEnabled = true
            )
        }

        // Page content
        content()
    }
}
