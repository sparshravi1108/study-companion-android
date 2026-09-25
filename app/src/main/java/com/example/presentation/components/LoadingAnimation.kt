package com.example.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AiCompanionState
import com.example.ui.theme.StudyTheme

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    size: Dp = 72.dp,
    label: String? = "Loading...",
    color: Color? = null,
    testTag: String = "loading_animation"
) {
    val extendedColors = StudyTheme.extendedColors
    val config = StudyTheme.animationConfig
    val primaryColor = color ?: extendedColors.primary

    val infiniteTransition = rememberInfiniteTransition(label = "LoadingSpin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = config.duration(1200), easing = LinearEasing)
        ),
        label = "LoadingRotation"
    )

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = config.duration(800), easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "LoadingPulse"
    )

    Column(
        modifier = modifier.testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(size)) {
                val center = Offset(this.size.width / 2f, this.size.height / 2f)
                val radius = this.size.minDimension / 2f * 0.85f

                if (config.reduceMotion) {
                    drawCircle(
                        color = primaryColor,
                        radius = radius,
                        center = center,
                        style = Stroke(width = 3f)
                    )
                } else {
                    // Outer rotating segmented energy ring
                    rotate(rotation, pivot = center) {
                        drawArc(
                            color = primaryColor.copy(alpha = pulseAlpha),
                            startAngle = 0f,
                            sweepAngle = 260f,
                            useCenter = false,
                            topLeft = Offset(center.x - radius, center.y - radius),
                            size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                            style = Stroke(width = 3.5f, cap = StrokeCap.Round)
                        )
                    }

                    // Inner counter rotating energy ring
                    rotate(-rotation * 1.5f, pivot = center) {
                        drawArc(
                            color = extendedColors.secondary.copy(alpha = 0.7f),
                            startAngle = 180f,
                            sweepAngle = 140f,
                            useCenter = false,
                            topLeft = Offset(center.x - radius * 0.65f, center.y - radius * 0.65f),
                            size = androidx.compose.ui.geometry.Size(radius * 1.3f, radius * 1.3f),
                            style = Stroke(width = 2.5f, cap = StrokeCap.Round)
                        )
                    }

                    // Center glowing dot
                    drawCircle(
                        color = Color.White.copy(alpha = pulseAlpha),
                        radius = 3.5f,
                        center = center
                    )
                }
            }
        }

        if (label != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = extendedColors.textSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
