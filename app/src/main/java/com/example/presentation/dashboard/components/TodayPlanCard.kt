package com.example.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.dashboard.model.DashboardTask
import com.example.domain.dashboard.model.ProgressMetrics
import com.example.domain.dashboard.model.TaskStatus
import com.example.presentation.components.GlowCard
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.StudySpacing
import com.example.ui.theme.StudyTheme
import com.example.ui.theme.SuccessGreen

@Composable
fun TodayPlanCard(
    tasks: List<DashboardTask>,
    progress: ProgressMetrics,
    onTaskClick: (String) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors

    val remainingHours = progress.remainingMinutes / 60
    val remainingMins = progress.remainingMinutes % 60
    val remainingStr = if (remainingHours > 0) "${remainingHours}h ${remainingMins}m remaining" else "${remainingMins}m remaining"

    GlowCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("today_plan_card"),
        glowEnabled = true
    ) {
        Column {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(extendedColors.primary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = extendedColors.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Today's Plan",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = extendedColors.textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                }

                Text(
                    text = remainingStr,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = extendedColors.textTertiary,
                        fontSize = 11.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Sub-progress text
            Text(
                text = "${progress.completedTasks} / ${progress.totalTasks} completed",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = extendedColors.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp
                ),
                modifier = Modifier.padding(start = 36.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (tasks.isEmpty()) {
                Text(
                    text = "No study sessions planned for today.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = extendedColors.textSecondary,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            } else {
                tasks.take(4).forEach { task ->
                    TodayPlanItemRow(
                        task = task,
                        onClick = { onTaskClick(task.id) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // View all tasks action
            Row(
                modifier = Modifier
                    .clickable(onClick = onViewAllClick)
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "View all tasks →",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = extendedColors.primary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

@Composable
fun TodayPlanItemRow(
    task: DashboardTask,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors
    val isCompleted = task.status == TaskStatus.COMPLETED
    val isInProgress = task.status == TaskStatus.IN_PROGRESS

    val (badgeBg, badgeText, badgeColor) = when (task.status) {
        TaskStatus.COMPLETED -> Triple(SuccessGreen.copy(alpha = 0.18f), "Completed", SuccessGreen)
        TaskStatus.IN_PROGRESS -> Triple(ElectricBlue.copy(alpha = 0.18f), "In Progress", ElectricBlue)
        TaskStatus.PENDING -> Triple(extendedColors.surfaceHighlight, "Pending", extendedColors.textSecondary)
        TaskStatus.MISSED -> Triple(Color(0xFFEF4444).copy(alpha = 0.18f), "Missed", Color(0xFFEF4444))
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(extendedColors.surface.copy(alpha = 0.6f))
            .border(1.dp, extendedColors.border.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Status circle icon
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(
                    if (isCompleted) SuccessGreen
                    else if (isInProgress) ElectricBlue
                    else extendedColors.surfaceHighlight
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
            } else if (isInProgress) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "In Progress",
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
            } else {
                Icon(
                    imageVector = if (task.type == "Notes") Icons.Default.NoteAlt else Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = "Pending",
                    tint = extendedColors.textTertiary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Title and duration
        Column(modifier = Modifier.weight(1f)) {
            val titleText = if (!task.subjectName.isNullOrBlank()) "${task.subjectName} — ${task.title}" else task.title
            Text(
                text = titleText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (isCompleted) extendedColors.textTertiary else extendedColors.textPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                ),
                maxLines = 1
            )
            val scheduleDetail = buildString {
                append("${task.estimatedMinutes} min")
                if (!task.scheduledStart.isNullOrBlank() && !task.scheduledEnd.isNullOrBlank()) {
                    append("  •  ${task.scheduledStart} - ${task.scheduledEnd}")
                }
            }
            Text(
                text = scheduleDetail,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = extendedColors.textTertiary,
                    fontSize = 11.sp
                )
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Status pill badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(badgeBg)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = badgeText,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = badgeColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
