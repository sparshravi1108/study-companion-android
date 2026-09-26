package com.example.presentation.dashboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.dashboard.model.ProgressMetrics
import com.example.presentation.components.GlowCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.StudyTheme

@Composable
fun ProgressOverviewCard(
    progress: ProgressMetrics,
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    GlowCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("progress_overview_card"),
        glowEnabled = false
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(extendedColors.primary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = null,
                        tint = extendedColors.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Progress Overview",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = extendedColors.textPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Circular Progress Dial
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier.size(68.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(68.dp)) {
                            val strokeWidth = 6.dp.toPx()
                            val radius = (size.minDimension - strokeWidth) / 2
                            val center = Offset(size.width / 2, size.height / 2)

                            // Track
                            drawCircle(
                                color = extendedColors.border,
                                radius = radius,
                                center = center,
                                style = Stroke(width = strokeWidth)
                            )

                            // Active Arc
                            val sweepAngle = 360f * (progress.completionPercentage / 100f).coerceIn(0f, 1f)
                            if (sweepAngle > 0f) {
                                drawArc(
                                    brush = extendedColors.accentGradient,
                                    startAngle = -90f,
                                    sweepAngle = sweepAngle,
                                    useCenter = false,
                                    topLeft = Offset(center.x - radius, center.y - radius),
                                    size = Size(radius * 2, radius * 2),
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                )
                            }
                        }

                        Text(
                            text = "${progress.completionPercentage}%",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = extendedColors.textPrimary,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Today's Progress",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = extendedColors.textPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        )
                        Text(
                            text = "${progress.completedTasks} / ${progress.totalTasks} tasks completed",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = extendedColors.textTertiary,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                // Weekly 7-Bar Chart
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Weekly Progress",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = extendedColors.textSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.height(48.dp)
                    ) {
                        days.forEachIndexed { index, day ->
                            val minutes = progress.weeklyDayMinutes.getOrElse(index) { 0 }
                            val maxMin = 90f
                            val heightFraction = (minutes / maxMin).coerceIn(0.15f, 1f)
                            val isToday = index == 3 // Thu in blueprint

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.height(48.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(width = 8.dp, height = (36 * heightFraction).dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            if (isToday) NeonCyan
                                            else if (minutes > 0) extendedColors.primary
                                            else extendedColors.surfaceHighlight
                                        )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isToday) NeonCyan else extendedColors.textTertiary,
                                        fontSize = 8.sp,
                                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
