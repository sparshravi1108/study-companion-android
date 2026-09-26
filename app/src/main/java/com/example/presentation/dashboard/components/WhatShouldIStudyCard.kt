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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.dashboard.model.StudyRecommendation
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.StudyRadii
import com.example.ui.theme.StudySpacing
import com.example.ui.theme.StudyTheme

@Composable
fun WhatShouldIStudyCard(
    recommendation: StudyRecommendation?,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors
    val config = StudyTheme.animationConfig

    val cardShape = RoundedCornerShape(StudyRadii.large)
    val glowBorderBrush = Brush.linearGradient(
        listOf(
            NeonCyan,
            NeonPurple,
            NeonPink
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("what_should_i_study_section")
    ) {
        // Section Header with neon spark icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = NeonCyan,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "What should you study now?",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = extendedColors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if (config.reduceMotion) 0.dp else 12.dp * config.glowIntensity,
                    shape = cardShape,
                    spotColor = NeonPurple.copy(alpha = 0.45f * config.glowIntensity),
                    ambientColor = Color.Black.copy(alpha = 0.5f)
                )
                .clip(cardShape)
                .background(extendedColors.surface)
                .border(1.5.dp, glowBorderBrush, cardShape)
                .clickable(onClick = onActionClick)
                .padding(StudySpacing.md)
        ) {
            if (recommendation != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left science badge
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        NeonCyan.copy(alpha = 0.25f),
                                        NeonPurple.copy(alpha = 0.35f)
                                    )
                                )
                            )
                            .border(1.dp, NeonCyan.copy(alpha = 0.6f), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "Subject Icon",
                            tint = NeonCyan,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    // Middle content
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = recommendation.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = extendedColors.textPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = recommendation.urgencyText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = extendedColors.primary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = recommendation.reason,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = extendedColors.textTertiary,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Right arrow action button
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(extendedColors.accentGradient)
                            .testTag("recommendation_arrow_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Start Recommended Action",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            } else {
                // Empty state for recommendation
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Nothing urgent right now.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = extendedColors.textPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Add a task or study session to get priority guidance.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = extendedColors.textTertiary,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}
