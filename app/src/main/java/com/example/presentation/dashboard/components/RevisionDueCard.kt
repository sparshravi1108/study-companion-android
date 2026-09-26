package com.example.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.MenuBook
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.dashboard.model.PriorityLevel
import com.example.domain.dashboard.model.RevisionItem
import com.example.presentation.components.GlowCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.StudyTheme

@Composable
fun RevisionDueCard(
    revisions: List<RevisionItem>,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors

    GlowCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("revision_due_card"),
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
                        imageVector = Icons.Default.Autorenew,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Revision Due",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = extendedColors.textPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (revisions.isEmpty()) {
                Text(
                    text = "No revision due.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = extendedColors.textSecondary,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            } else {
                revisions.take(3).forEach { rev ->
                    RevisionItemRow(revision = rev)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .clickable(onClick = onViewAllClick)
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "View all →",
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
fun RevisionItemRow(
    revision: RevisionItem,
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors

    val (badgeBg, badgeText, badgeColor) = when (revision.priority) {
        PriorityLevel.HIGH, PriorityLevel.URGENT -> Triple(Color(0xFFF43F5E).copy(alpha = 0.2f), "High", Color(0xFFF43F5E))
        PriorityLevel.MEDIUM -> Triple(Color(0xFFF59E0B).copy(alpha = 0.2f), "Medium", Color(0xFFF59E0B))
        PriorityLevel.LOW -> Triple(Color(0xFF3B82F6).copy(alpha = 0.2f), "Low", Color(0xFF60A5FA))
    }

    val topicTitle = if (!revision.subjectName.isNullOrBlank()) "${revision.subjectName} — ${revision.topic}" else revision.topic

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(badgeBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MenuBook,
                contentDescription = null,
                tint = badgeColor,
                modifier = Modifier.size(15.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = topicTitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = extendedColors.textPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                ),
                maxLines = 1
            )
            Text(
                text = "${revision.dueText} • ${revision.estimatedMinutes} min",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = extendedColors.textTertiary,
                    fontSize = 10.sp
                )
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(badgeBg)
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = badgeText,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = badgeColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
