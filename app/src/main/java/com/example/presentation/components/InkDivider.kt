package com.example.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.StudyTheme

@Composable
fun InkDivider(
    modifier: Modifier = Modifier,
    height: Dp = 14.dp,
    color: Color? = null,
    showEnergyPulse: Boolean = true
) {
    val extendedColors = StudyTheme.extendedColors
    val config = StudyTheme.animationConfig
    val dividerColor = color ?: extendedColors.borderHighlight

    val infiniteTransition = rememberInfiniteTransition(label = "InkDividerTransition")
    val pulseProgress by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = config.duration(1800),
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "DividerPulse"
    )

    val effectiveAlpha = if (config.reduceMotion || !showEnergyPulse) 0.6f else pulseProgress * 0.85f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(height)) {
            val midY = size.height / 2f
            val width = size.width

            // Gradient line fading at the ends (ink brush style)
            val lineBrush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    dividerColor.copy(alpha = effectiveAlpha * 0.2f),
                    dividerColor.copy(alpha = effectiveAlpha),
                    extendedColors.energyColor.copy(alpha = effectiveAlpha),
                    dividerColor.copy(alpha = effectiveAlpha),
                    dividerColor.copy(alpha = effectiveAlpha * 0.2f),
                    Color.Transparent
                )
            )

            // Stylized central ink/lightning jagged accent
            val path = Path().apply {
                moveTo(0f, midY)
                lineTo(width * 0.42f, midY)
                lineTo(width * 0.47f, midY - 3f)
                lineTo(width * 0.50f, midY + 4f)
                lineTo(width * 0.53f, midY - 2f)
                lineTo(width * 0.58f, midY)
                lineTo(width, midY)
            }

            drawPath(
                path = path,
                brush = lineBrush,
                style = Stroke(
                    width = 1.8f,
                    cap = StrokeCap.Round
                )
            )

            // Central glowing energy node
            if (showEnergyPulse && !config.reduceMotion) {
                drawCircle(
                    color = extendedColors.energyColor.copy(alpha = effectiveAlpha),
                    radius = 2.5f,
                    center = Offset(width * 0.50f, midY + 4f)
                )
            }
        }
    }
}
