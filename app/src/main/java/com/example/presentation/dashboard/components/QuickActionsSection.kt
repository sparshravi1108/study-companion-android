package com.example.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.components.GlowCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.StudyTheme

@Composable
fun QuickActionsSection(
    onStartFocus: () -> Unit,
    onAddTask: () -> Unit,
    onAddSubject: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors

    GlowCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("quick_actions_card"),
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
                        .background(NeonCyan.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = extendedColors.textPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Row 1: Active Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionItem(
                    label = "Start Focus",
                    icon = Icons.Default.Timer,
                    iconTint = Color(0xFF38BDF8),
                    onClick = onStartFocus,
                    modifier = Modifier.weight(1f)
                )
                QuickActionItem(
                    label = "Add Task",
                    icon = Icons.Default.Add,
                    iconTint = Color(0xFFA855F7),
                    onClick = onAddTask,
                    modifier = Modifier.weight(1f)
                )
                QuickActionItem(
                    label = "Add Subject",
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    iconTint = Color(0xFF3B82F6),
                    onClick = onAddSubject,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Row 2: Future Phase Actions (Disabled / Coming Soon)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionItem(
                    label = "Scan Question",
                    icon = Icons.Default.DocumentScanner,
                    iconTint = extendedColors.textTertiary,
                    isComingSoon = true,
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
                QuickActionItem(
                    label = "Ask AI",
                    icon = Icons.Default.Psychology,
                    iconTint = extendedColors.textTertiary,
                    isComingSoon = true,
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
                QuickActionItem(
                    label = "Add Project",
                    icon = Icons.Default.Folder,
                    iconTint = extendedColors.textTertiary,
                    isComingSoon = true,
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun QuickActionItem(
    label: String,
    icon: ImageVector,
    iconTint: Color,
    isComingSoon: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .background(extendedColors.surfaceHighlight.copy(alpha = if (isComingSoon) 0.3f else 0.7f))
            .border(
                1.dp,
                if (isComingSoon) extendedColors.border.copy(alpha = 0.3f) else extendedColors.borderHighlight.copy(alpha = 0.4f),
                shape
            )
            .clickable(
                enabled = !isComingSoon,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = extendedColors.primary),
                onClick = onClick
            )
            .padding(vertical = 12.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = if (isComingSoon) 0.1f else 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = if (isComingSoon) extendedColors.textTertiary else extendedColors.textPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp
                ),
                maxLines = 1
            )

            if (isComingSoon) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Coming Soon",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF38BDF8).copy(alpha = 0.8f),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
