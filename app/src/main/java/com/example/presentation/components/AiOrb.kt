package com.example.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.domain.model.AiCompanionState
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.LightningAmber
import com.example.ui.theme.LightningGold
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.StudyTheme
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AiOrb(
    state: AiCompanionState,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    onClick: (() -> Unit)? = null
) {
    val config = StudyTheme.animationConfig
    val extendedColors = StudyTheme.extendedColors
    val reduceMotion = config.reduceMotion

    val infiniteTransition = rememberInfiniteTransition(label = "AiOrbTransition")

    // Breathing pulse scale
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = if (reduceMotion) 1f else when (state) {
            AiCompanionState.FOCUS -> 0.98f
            AiCompanionState.LISTENING -> 0.90f
            AiCompanionState.SPEAKING -> 0.88f
            AiCompanionState.WARNING -> 0.92f
            else -> 0.93f
        },
        targetValue = if (reduceMotion) 1f else when (state) {
            AiCompanionState.FOCUS -> 1.02f
            AiCompanionState.LISTENING -> 1.12f
            AiCompanionState.SPEAKING -> 1.14f
            AiCompanionState.WARNING -> 1.08f
            else -> 1.07f
        },
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = when (state) {
                    AiCompanionState.LISTENING -> config.duration(900)
                    AiCompanionState.SPEAKING -> config.duration(700)
                    AiCompanionState.WARNING -> config.duration(1100)
                    AiCompanionState.FOCUS -> config.duration(3200)
                    else -> config.duration(1800)
                },
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    // Orbital rotation
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = when (state) {
                    AiCompanionState.THINKING -> config.duration(3500)
                    AiCompanionState.FOCUS -> config.duration(16000)
                    else -> config.duration(8000)
                },
                easing = LinearEasing
            )
        ),
        label = "OrbRotation"
    )

    // Secondary pulse for speaking / listening waves
    val wavePulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(config.duration(1000), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "WavePulse"
    )

    val (coreColor, ringColor, accentColor) = remember(state, extendedColors) {
        when (state) {
            AiCompanionState.IDLE -> Triple(extendedColors.orbCore, extendedColors.orbRing, extendedColors.primary)
            AiCompanionState.LISTENING -> Triple(NeonCyan, Color(0xFF38BDF8), Color(0xFF67E8F9))
            AiCompanionState.THINKING -> Triple(NeonPurple, NeonPink, NeonCyan)
            AiCompanionState.TEACHING -> Triple(Color(0xFF38BDF8), NeonCyan, LightningGold)
            AiCompanionState.SPEAKING -> Triple(NeonPink, NeonPurple, Color(0xFFF472B6))
            AiCompanionState.SUCCESS -> Triple(SuccessGreen, Color(0xFF34D399), NeonCyan)
            AiCompanionState.WARNING -> Triple(WarningAmber, ErrorRed, LightningAmber)
            AiCompanionState.FOCUS -> Triple(Color(0xFF14B8A6), Color(0xFF3B82F6), Color(0xFF06B6D4))
            AiCompanionState.CELEBRATION -> Triple(LightningGold, NeonPink, SuccessGreen)
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .testTag("ai_orb_component")
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false, radius = size / 1.5f),
                        onClick = onClick
                    )
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val baseRadius = this.size.minDimension / 2f

            // Outer atmospheric diffuse glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ringColor.copy(alpha = 0.40f * config.glowIntensity),
                        coreColor.copy(alpha = 0.12f * config.glowIntensity),
                        Color.Transparent
                    ),
                    center = center,
                    radius = baseRadius * pulseScale
                ),
                radius = baseRadius * pulseScale,
                center = center
            )

            // State-specific exterior rings:
            // 1. Speaking / Listening: radiating concentric wave rings
            if ((state == AiCompanionState.SPEAKING || state == AiCompanionState.LISTENING) && !reduceMotion) {
                drawCircle(
                    color = accentColor.copy(alpha = (1f - wavePulse) * 0.7f * config.glowIntensity),
                    radius = baseRadius * (0.8f + 0.25f * wavePulse),
                    center = center,
                    style = Stroke(width = 1.5f)
                )
            }

            // 2. Outer rotating cybernetic dashed energy ring (Idle, Thinking, Teaching, etc.)
            if (!reduceMotion && config.enableContinuousAnimations) {
                rotate(rotationAngle, pivot = center) {
                    drawCircle(
                        color = ringColor.copy(alpha = 0.75f * config.glowIntensity),
                        radius = baseRadius * 0.80f,
                        center = center,
                        style = Stroke(
                            width = 2.2f,
                            pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                                if (state == AiCompanionState.THINKING) floatArrayOf(8f, 6f)
                                else floatArrayOf(14f, 8f, 4f, 8f),
                                0f
                            )
                        )
                    )

                    // 3 Orbiting energy nodes
                    val nodeCount = if (state == AiCompanionState.THINKING) 4 else 3
                    val stepAngle = 360.0 / nodeCount
                    for (i in 0 until nodeCount) {
                        val angleRad = Math.toRadians((i * stepAngle))
                        val nodeX = center.x + (baseRadius * 0.80f) * cos(angleRad).toFloat()
                        val nodeY = center.y + (baseRadius * 0.80f) * sin(angleRad).toFloat()
                        drawCircle(
                            color = accentColor,
                            radius = 3.0f,
                            center = Offset(nodeX, nodeY)
                        )
                    }
                }

                // Inner counter-rotating ring
                rotate(-rotationAngle * 1.4f, pivot = center) {
                    drawCircle(
                        color = coreColor.copy(alpha = 0.55f * config.glowIntensity),
                        radius = baseRadius * 0.60f,
                        center = center,
                        style = Stroke(
                            width = 1.8f,
                            pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                                floatArrayOf(6f, 6f),
                                0f
                            )
                        )
                    )
                }
            } else {
                // Static elegant ring for reduce motion
                drawCircle(
                    color = ringColor.copy(alpha = 0.6f),
                    radius = baseRadius * 0.75f,
                    center = center,
                    style = Stroke(width = 2f)
                )
            }

            // Glowing Core AI Orb
            val coreRadius = baseRadius * 0.42f * pulseScale
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White,
                        coreColor,
                        ringColor
                    ),
                    center = Offset(center.x - baseRadius * 0.1f, center.y - baseRadius * 0.1f),
                    radius = coreRadius
                ),
                radius = coreRadius,
                center = center
            )

            // Special Teaching / Academic Crest Glyph inside core
            if (state == AiCompanionState.TEACHING) {
                val bookPath = Path().apply {
                    val s = coreRadius * 0.45f
                    moveTo(center.x - s, center.y - s * 0.5f)
                    lineTo(center.x, center.y - s * 0.1f)
                    lineTo(center.x + s, center.y - s * 0.5f)
                    lineTo(center.x + s, center.y + s * 0.5f)
                    lineTo(center.x, center.y + s * 0.9f)
                    lineTo(center.x - s, center.y + s * 0.5f)
                    close()
                }
                drawPath(
                    path = bookPath,
                    color = Color.White.copy(alpha = 0.85f),
                    style = Stroke(width = 1.6f)
                )
            } else {
                // Core highlight sheen
                drawCircle(
                    color = Color.White.copy(alpha = 0.80f),
                    radius = baseRadius * 0.11f,
                    center = Offset(center.x - baseRadius * 0.13f, center.y - baseRadius * 0.13f)
                )
            }
        }
    }
}
