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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.dashboard.model.DashboardTask
import com.example.domain.dashboard.model.PriorityLevel
import com.example.domain.dashboard.model.TaskStatus
import com.example.presentation.components.NeonButton
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.StudySpacing
import com.example.ui.theme.StudyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheet(
    onDismiss: () -> Unit,
    onSaveTask: (DashboardTask) -> Unit
) {
    val extendedColors = StudyTheme.extendedColors
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var title by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf("Mathematics") }
    var selectedType by remember { mutableStateOf("Practice") }
    var selectedDuration by remember { mutableIntStateOf(45) }
    var selectedPriority by remember { mutableStateOf(PriorityLevel.HIGH) }

    val subjects = listOf("Mathematics", "Physics", "Chemistry", "Biology", "Computer Science")
    val types = listOf("Practice", "Revision", "Notes", "Reading")
    val durations = listOf(15, 30, 45, 60)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = extendedColors.surface,
        dragHandle = null,
        modifier = Modifier.testTag("add_task_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = StudySpacing.lg, vertical = StudySpacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Add Study Task",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = extendedColors.textPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = extendedColors.textSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task Title e.g. Calculus Practice") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("task_title_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = extendedColors.border,
                    focusedTextColor = extendedColors.textPrimary,
                    unfocusedTextColor = extendedColors.textPrimary
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Subject Selector
            Text(
                text = "Subject",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = extendedColors.textSecondary
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                subjects.take(3).forEach { s ->
                    SubjectChip(
                        name = s,
                        isSelected = selectedSubject == s,
                        onClick = { selectedSubject = s }
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                subjects.drop(3).forEach { s ->
                    SubjectChip(
                        name = s,
                        isSelected = selectedSubject == s,
                        onClick = { selectedSubject = s }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Duration Selector
            Text(
                text = "Duration",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = extendedColors.textSecondary
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                durations.forEach { d ->
                    DurationChip(
                        minutes = d,
                        isSelected = selectedDuration == d,
                        onClick = { selectedDuration = d }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Priority Selector
            Text(
                text = "Priority",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = extendedColors.textSecondary
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PriorityLevel.entries.forEach { p ->
                    PriorityChip(
                        priority = p,
                        isSelected = selectedPriority == p,
                        onClick = { selectedPriority = p }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            NeonButton(
                text = "Save Task",
                onClick = {
                    if (title.isNotBlank()) {
                        val task = DashboardTask(
                            id = "task_${System.currentTimeMillis()}",
                            title = title.trim(),
                            type = selectedType,
                            subjectName = selectedSubject,
                            estimatedMinutes = selectedDuration,
                            status = TaskStatus.PENDING,
                            priority = selectedPriority,
                            dueTimestamp = System.currentTimeMillis() + 24 * 60 * 60 * 1000L
                        )
                        onSaveTask(task)
                        onDismiss()
                    }
                },
                enabled = title.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                testTag = "save_new_task_button"
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SubjectChip(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val extendedColors = StudyTheme.extendedColors
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) extendedColors.primary.copy(alpha = 0.25f) else extendedColors.surfaceHighlight)
            .border(
                1.dp,
                if (isSelected) extendedColors.borderHighlight else extendedColors.border,
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isSelected) extendedColors.primary else extendedColors.textSecondary,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}

@Composable
private fun DurationChip(
    minutes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val extendedColors = StudyTheme.extendedColors
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) extendedColors.primary.copy(alpha = 0.25f) else extendedColors.surfaceHighlight)
            .border(
                1.dp,
                if (isSelected) extendedColors.borderHighlight else extendedColors.border,
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = "$minutes min",
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isSelected) extendedColors.primary else extendedColors.textSecondary,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}

@Composable
private fun PriorityChip(
    priority: PriorityLevel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val extendedColors = StudyTheme.extendedColors
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) extendedColors.primary.copy(alpha = 0.25f) else extendedColors.surfaceHighlight)
            .border(
                1.dp,
                if (isSelected) extendedColors.borderHighlight else extendedColors.border,
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = priority.label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isSelected) extendedColors.primary else extendedColors.textSecondary,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}
