package com.example.presentation.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AnimationLevel
import com.example.ui.theme.StudyRadii
import com.example.ui.theme.StudySpacing
import com.example.ui.theme.StudyTheme

@Composable
fun AnimationIntensitySelector(
    currentLevel: AnimationLevel,
    onLevelSelected: (AnimationLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AnimationLevel.values().forEach { level ->
            val isSelected = currentLevel == level
            val shape = RoundedCornerShape(StudyRadii.small)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("anim_level_${level.name.lowercase()}")
                    .clip(shape)
                    .background(if (isSelected) extendedColors.primary.copy(alpha = 0.12f) else extendedColors.surface)
                    .border(
                        1.2.dp,
                        if (isSelected) extendedColors.borderHighlight else extendedColors.border,
                        shape
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = extendedColors.primary),
                        onClick = { onLevelSelected(level) }
                    )
                    .padding(StudySpacing.sm + 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = level.label,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = if (isSelected) extendedColors.primary else extendedColors.textPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${(level.multiplier * 100).toInt()}% speed",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = extendedColors.textTertiary,
                                    fontSize = 10.sp
                                )
                            )
                        }
                        Text(
                            text = level.description,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = extendedColors.textSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }

                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(extendedColors.primary)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "Active",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReduceMotionToggle(
    reduceMotion: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Reduce Motion",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = extendedColors.textPrimary,
                    fontWeight = FontWeight.Medium
                )
            )
            Text(
                text = "Minimizes motion and particles for better focus and battery",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = extendedColors.textTertiary,
                    fontSize = 11.sp
                )
            )
        }
        Switch(
            checked = reduceMotion,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = extendedColors.primary,
                uncheckedThumbColor = extendedColors.textTertiary,
                uncheckedTrackColor = extendedColors.surface
            ),
            modifier = Modifier.testTag("reduce_motion_switch")
        )
    }
}
