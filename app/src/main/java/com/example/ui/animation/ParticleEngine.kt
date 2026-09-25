package com.example.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.StudyTheme
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

enum class ParticlePreset(
    val baseCount: Int,
    val speedMultiplier: Float,
    val sizeRange: ClosedFloatingPointRange<Float>,
    val driftSpeed: Float
) {
    SUBTLE(baseCount = 10, speedMultiplier = 0.5f, sizeRange = 1.5f..3.0f, driftSpeed = 0.3f),
    SUCCESS(baseCount = 24, speedMultiplier = 1.4f, sizeRange = 2.5f..5.5f, driftSpeed = 0.8f),
    ENERGY(baseCount = 16, speedMultiplier = 0.8f, sizeRange = 2.0f..4.0f, driftSpeed = 0.5f),
    LIGHTNING(baseCount = 18, speedMultiplier = 1.6f, sizeRange = 1.8f..4.0f, driftSpeed = 1.2f),
    INK(baseCount = 12, speedMultiplier = 0.3f, sizeRange = 3.0f..7.0f, driftSpeed = 0.2f),
    FOCUS(baseCount = 6, speedMultiplier = 0.2f, sizeRange = 1.0f..2.5f, driftSpeed = 0.1f),
    WARNING(baseCount = 14, speedMultiplier = 0.9f, sizeRange = 2.0f..4.5f, driftSpeed = 0.6f)
}

data class Particle(
    val xRatio: Float,
    val yRatio: Float,
    val size: Float,
    val speed: Float,
    val angleRad: Float,
    val baseAlpha: Float,
    val color: Color
)

@Composable
fun ParticleEffect(
    modifier: Modifier = Modifier,
    preset: ParticlePreset = ParticlePreset.SUBTLE,
    overrideCount: Int? = null,
    color: Color? = null,
    isEnabled: Boolean = true
) {
    val config = StudyTheme.animationConfig
    val extendedColors = StudyTheme.extendedColors

    if (!isEnabled || !config.enableParticles || config.reduceMotion) {
        return
    }

    val particleColor = color ?: when (preset) {
        ParticlePreset.SUBTLE -> extendedColors.glowSoft
        ParticlePreset.SUCCESS -> SuccessGreen
        ParticlePreset.ENERGY -> extendedColors.primary
        ParticlePreset.LIGHTNING -> extendedColors.energyColor
        ParticlePreset.INK -> extendedColors.glowAi
        ParticlePreset.FOCUS -> extendedColors.glowSoft
        ParticlePreset.WARNING -> WarningAmber
    }

    val effectiveCount = (overrideCount ?: (preset.baseCount * config.particleDensity).toInt())
        .coerceIn(4, 30)

    val particles = remember(preset, effectiveCount, particleColor) {
        val rand = Random(42 + preset.ordinal)
        List(effectiveCount) {
            Particle(
                xRatio = rand.nextFloat(),
                yRatio = rand.nextFloat(),
                size = rand.nextFloat() * (preset.sizeRange.endInclusive - preset.sizeRange.start) + preset.sizeRange.start,
                speed = (0.2f + rand.nextFloat() * 0.8f) * preset.speedMultiplier,
                angleRad = rand.nextFloat() * 2f * Math.PI.toFloat(),
                baseAlpha = 0.25f + rand.nextFloat() * 0.5f,
                color = particleColor
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "ParticleAnimation")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = config.duration(6000), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ParticleProgress"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        if (width <= 0f || height <= 0f) return@Canvas

        particles.forEach { p ->
            val totalDistance = animationProgress * p.speed * 60f * preset.driftSpeed
            val currentX = (p.xRatio * width + cos(p.angleRad) * totalDistance) % width
            val currentY = (p.yRatio * height + sin(p.angleRad) * totalDistance) % height

            val normalizedX = if (currentX < 0) currentX + width else currentX
            val normalizedY = if (currentY < 0) currentY + height else currentY

            val pulsingAlpha = (p.baseAlpha * (0.6f + 0.4f * sin(animationProgress * 6.28f + p.xRatio * 10f)))
                .coerceIn(0.1f, 0.85f) * config.glowIntensity

            drawCircle(
                color = p.color.copy(alpha = pulsingAlpha),
                radius = p.size,
                center = Offset(normalizedX, normalizedY)
            )
        }
    }
}

@Composable
fun SuccessBurst(
    modifier: Modifier = Modifier,
    size: Dp = 90.dp,
    showCheckmark: Boolean = true,
    onComplete: (() -> Unit)? = null
) {
    val config = StudyTheme.animationConfig
    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = config.duration(650),
                easing = FastOutSlowInEasing
            )
        )
        onComplete?.invoke()
    }

    val progress = animProgress.value
    val outerRadius = (size.value * 0.45f) * progress
    val alpha = (1f - progress).coerceIn(0f, 1f)

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)

            // Expanding energy ring
            drawCircle(
                color = SuccessGreen.copy(alpha = alpha * 0.8f),
                radius = outerRadius * 1.1f,
                center = center,
                style = Stroke(width = 3f * (1f - progress * 0.5f))
            )

            // Radiating particles (8 radial points)
            for (i in 0 until 8) {
                val angle = Math.toRadians((i * 45.0))
                val dist = outerRadius * 1.25f
                val pX = center.x + (dist * cos(angle)).toFloat()
                val pY = center.y + (dist * sin(angle)).toFloat()

                drawCircle(
                    color = NeonCyan.copy(alpha = alpha),
                    radius = (4f * (1f - progress * 0.7f)).coerceAtLeast(1f),
                    center = Offset(pX, pY)
                )
            }
        }

        if (showCheckmark) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Success",
                tint = SuccessGreen.copy(alpha = progress.coerceIn(0f, 1f)),
                modifier = Modifier.size(size * 0.45f)
            )
        }
    }
}
