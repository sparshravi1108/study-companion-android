package com.example.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.StudyTheme

@Composable
fun EnergyProgressBar(
    progress: Float, // 0.0f to 1.0f
    modifier: Modifier = Modifier,
    height: Dp = 10.dp,
    trackColor: Color? = null,
    progressGradient: Brush? = null,
    showGlowHead: Boolean = true,
    testTag: String = "energy_progress_bar"
) {
    val extendedColors = StudyTheme.extendedColors
    val config = StudyTheme.animationConfig

    val clampedProgress = progress.coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = clampedProgress,
        animationSpec = if (config.reduceMotion) tween(100) else tween(durationMillis = config.duration(600), easing = FastOutSlowInEasing),
        label = "EnergyProgress"
    )

    val effectiveTrackColor = trackColor ?: extendedColors.border
    val effectiveGradient = progressGradient ?: extendedColors.accentGradient
    val shape = RoundedCornerShape(height / 2)

    Box(
        modifier = modifier
            .testTag(testTag)
            .fillMaxWidth()
            .height(height)
            .shadow(
                elevation = if (config.reduceMotion) 0.dp else 4.dp * config.glowIntensity,
                shape = shape,
                spotColor = extendedColors.energyColor.copy(alpha = 0.5f * config.glowIntensity),
                ambientColor = Color.Transparent
            )
            .clip(shape)
            .background(effectiveTrackColor)
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(height)) {
            val progressWidth = size.width * animatedProgress
            val centerY = size.height / 2f

            if (progressWidth > 0f) {
                // Active energy line
                drawLine(
                    brush = effectiveGradient,
                    start = Offset(0f, centerY),
                    end = Offset(progressWidth, centerY),
                    strokeWidth = size.height,
                    cap = StrokeCap.Round
                )

                // Glowing energy node / particle at progress head
                if (showGlowHead && !config.reduceMotion && animatedProgress > 0.02f) {
                    drawCircle(
                        color = Color.White,
                        radius = size.height * 0.45f,
                        center = Offset(progressWidth - size.height * 0.2f, centerY)
                    )
                    drawCircle(
                        color = extendedColors.glowStrong.copy(alpha = 0.6f * config.glowIntensity),
                        radius = size.height * 0.9f,
                        center = Offset(progressWidth - size.height * 0.2f, centerY)
                    )
                }
            }
        }
    }
}
