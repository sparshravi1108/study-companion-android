package com.example.presentation.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AiCompanionState
import com.example.domain.model.UserProfile
import com.example.presentation.components.AiOrb
import com.example.presentation.components.AnimatedCard
import com.example.presentation.components.EnergyProgressBar
import com.example.presentation.components.GlowCard
import com.example.presentation.components.InkBackground
import com.example.presentation.components.InkDivider
import com.example.presentation.components.NeonButton
import com.example.presentation.components.NeonIconButton
import com.example.ui.animation.ParticleEffect
import com.example.ui.animation.ParticlePreset
import com.example.ui.animation.SuccessBurst
import com.example.ui.theme.StudyRadii
import com.example.ui.theme.StudySpacing
import com.example.ui.theme.StudyTheme

@Composable
fun HomeDashboardScreen(
    userProfile: UserProfile,
    isOnline: Boolean,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSubjects: () -> Unit = {},
    onNavigateToRoadmap: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var aiState by remember { mutableStateOf(AiCompanionState.IDLE) }
    var showSuccessBurst by remember { mutableStateOf(false) }
    var sampleProgress by remember { mutableFloatStateOf(0.72f) }
    var activeParticlePreset by remember { mutableStateOf(ParticlePreset.ENERGY) }
    var showParticlesDemo by remember { mutableStateOf(false) }

    val extendedColors = StudyTheme.extendedColors
    val config = StudyTheme.animationConfig

    InkBackground(
        modifier = modifier.testTag("home_dashboard_screen"),
        showParticles = true,
        particlePreset = ParticlePreset.SUBTLE
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = StudySpacing.lg, vertical = StudySpacing.md)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Good Morning ☀️",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = extendedColors.textSecondary,
                                fontSize = 13.sp
                            )
                        )
                    }
                    Text(
                        text = userProfile.displayName,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = extendedColors.textPrimary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp
                        )
                    )
                    Text(
                        text = "Mon, 28 Apr 2026",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = extendedColors.textTertiary,
                            fontSize = 12.sp
                        )
                    )
                }

                // AI Companion Header Avatar Orb
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .background(extendedColors.surface)
                        .border(1.5.dp, extendedColors.borderHighlight, CircleShape)
                        .testTag("dashboard_header_ai_orb"),
                    contentAlignment = Alignment.Center
                ) {
                    AiOrb(
                        state = aiState,
                        size = 58.dp,
                        onClick = {
                            aiState = when (aiState) {
                                AiCompanionState.IDLE -> AiCompanionState.LISTENING
                                AiCompanionState.LISTENING -> AiCompanionState.THINKING
                                AiCompanionState.THINKING -> AiCompanionState.TEACHING
                                AiCompanionState.TEACHING -> AiCompanionState.SPEAKING
                                AiCompanionState.SPEAKING -> AiCompanionState.FOCUS
                                AiCompanionState.FOCUS -> AiCompanionState.SUCCESS
                                AiCompanionState.SUCCESS -> AiCompanionState.WARNING
                                else -> AiCompanionState.IDLE
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Welcome to Study Companion Banner Card
            GlowCard(
                modifier = Modifier.fillMaxWidth(),
                glowEnabled = true,
                testTag = "welcome_study_banner"
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AiOrb(state = aiState, size = 36.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Welcome to Study Companion",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = extendedColors.textPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            )
                            Text(
                                text = "State: ${aiState.label} • Tap orb to cycle states",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = extendedColors.primary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Your AI-powered study partner is here to help you learn, plan and achieve your goals.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = extendedColors.textSecondary,
                            lineHeight = 20.sp,
                            fontSize = 13.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    NeonButton(
                        text = "Explore Features",
                        onClick = {
                            aiState = AiCompanionState.TEACHING
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        height = 42.dp,
                        testTag = "explore_features_button"
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Quick Category Access Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val row1 = listOf(
                    Triple("Study", Icons.AutoMirrored.Filled.MenuBook, onNavigateToSubjects),
                    Triple("Tasks", Icons.Default.Checklist, { aiState = AiCompanionState.FOCUS }),
                    Triple("Notes", Icons.Default.NoteAlt, { aiState = AiCompanionState.THINKING }),
                    Triple("AI", Icons.Default.FlashOn, { aiState = AiCompanionState.SPEAKING })
                )
                row1.forEach { (label, icon, onClick) ->
                    QuickTile(
                        label = label,
                        icon = icon,
                        onClick = onClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val row2 = listOf(
                    Triple("Roadmap", Icons.Default.AltRoute, onNavigateToRoadmap),
                    Triple("Calendar", Icons.Default.CalendarMonth, { aiState = AiCompanionState.FOCUS }),
                    Triple("Drive", Icons.Default.CloudDone, onNavigateToSettings),
                    Triple("More", Icons.Default.GridView, onNavigateToSettings)
                )
                row2.forEach { (label, icon, onClick) ->
                    QuickTile(
                        label = label,
                        icon = icon,
                        onClick = onClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Actions Section (from Phase 1 Blueprint)
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = extendedColors.textPrimary,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionChip(
                    label = "Start Focus",
                    icon = Icons.Default.Timer,
                    onClick = { aiState = AiCompanionState.FOCUS },
                    modifier = Modifier.weight(1f)
                )
                QuickActionChip(
                    label = "Add Task",
                    icon = Icons.Default.Add,
                    onClick = { showSuccessBurst = true },
                    modifier = Modifier.weight(1f)
                )
                QuickActionChip(
                    label = "Scan Question",
                    icon = Icons.Default.DocumentScanner,
                    onClick = { aiState = AiCompanionState.THINKING },
                    modifier = Modifier.weight(1f)
                )
                QuickActionChip(
                    label = "Ask AI",
                    icon = Icons.Default.Psychology,
                    onClick = { aiState = AiCompanionState.LISTENING },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Ink Divider
            InkDivider()

            Spacer(modifier = Modifier.height(22.dp))

            // Component Library Visual Showcase
            Text(
                text = "Phase 1 Component Library",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = extendedColors.textPrimary,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "Reusable visual tokens, cards, buttons, particles and energy bars",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = extendedColors.textTertiary,
                    fontSize = 11.sp
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Showcase Row 1: Neon Button & Neon Icon Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                NeonButton(
                    text = "Get Started →",
                    onClick = { showSuccessBurst = true },
                    modifier = Modifier.weight(1f),
                    height = 48.dp,
                    testTag = "sample_neon_button"
                )

                NeonIconButton(
                    icon = Icons.Default.Bolt,
                    onClick = {
                        sampleProgress = if (sampleProgress >= 1f) 0.2f else sampleProgress + 0.15f
                    },
                    contentDescription = "Trigger energy",
                    testTag = "sample_icon_button"
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Showcase Row 2: Glow Card & Animated Card
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GlowCard(
                    modifier = Modifier.weight(1f),
                    glowEnabled = true,
                    onClick = { }
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Mathematics",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = extendedColors.textPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            )
                            AiOrb(state = AiCompanionState.FOCUS, size = 22.dp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Progress 72%",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = extendedColors.primary,
                                fontSize = 11.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        EnergyProgressBar(progress = 0.72f, height = 6.dp)
                    }
                }

                AnimatedCard(
                    modifier = Modifier.weight(1f),
                    onClick = { }
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Physics",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = extendedColors.textPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(extendedColors.primary.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Learning",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = extendedColors.primary,
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Progress 45%",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = extendedColors.textTertiary,
                                fontSize = 11.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        EnergyProgressBar(progress = 0.45f, height = 6.dp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Showcase Row 3: Energy Progress Bar Live Demo
            GlowCard(
                modifier = Modifier.fillMaxWidth(),
                glowEnabled = false
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Study Progress (Tap + to test)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = extendedColors.textSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Text(
                            text = "${(sampleProgress * 100).toInt()}%",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = extendedColors.primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    EnergyProgressBar(
                        progress = sampleProgress,
                        height = 10.dp,
                        showGlowHead = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Showcase Row 4: Particle Effect & Success Burst Tester
            GlowCard(
                modifier = Modifier.fillMaxWidth(),
                glowEnabled = false
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Particle Engine & Success Burst",
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = extendedColors.textPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        NeonButton(
                            text = "Burst!",
                            onClick = { showSuccessBurst = true },
                            height = 34.dp,
                            modifier = Modifier.width(90.dp),
                            testTag = "trigger_success_burst"
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Preset chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ParticlePresetChip("Subtle", activeParticlePreset == ParticlePreset.SUBTLE) {
                            activeParticlePreset = ParticlePreset.SUBTLE
                        }
                        ParticlePresetChip("Energy", activeParticlePreset == ParticlePreset.ENERGY) {
                            activeParticlePreset = ParticlePreset.ENERGY
                        }
                        ParticlePresetChip("Lightning", activeParticlePreset == ParticlePreset.LIGHTNING) {
                            activeParticlePreset = ParticlePreset.LIGHTNING
                        }
                        ParticlePresetChip("Ink", activeParticlePreset == ParticlePreset.INK) {
                            activeParticlePreset = ParticlePreset.INK
                        }
                    }

                    if (showSuccessBurst) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            SuccessBurst(
                                onComplete = { showSuccessBurst = false }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // AI Orb 8 States Selector
            GlowCard(
                modifier = Modifier.fillMaxWidth(),
                glowEnabled = false
            ) {
                Column {
                    Text(
                        text = "AI Orb States (Tap to preview)",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = extendedColors.textPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val states = listOf(
                        AiCompanionState.IDLE,
                        AiCompanionState.LISTENING,
                        AiCompanionState.THINKING,
                        AiCompanionState.TEACHING,
                        AiCompanionState.SPEAKING,
                        AiCompanionState.SUCCESS,
                        AiCompanionState.WARNING,
                        AiCompanionState.FOCUS
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        states.take(4).forEach { s ->
                            AiStateItem(
                                state = s,
                                isSelected = aiState == s,
                                onClick = { aiState = s }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        states.drop(4).forEach { s ->
                            AiStateItem(
                                state = s,
                                isSelected = aiState == s,
                                onClick = { aiState = s }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun QuickTile(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors
    val shape = RoundedCornerShape(StudyRadii.small)

    Box(
        modifier = modifier
            .testTag("quick_tile_${label.lowercase()}")
            .clip(shape)
            .background(extendedColors.surface)
            .border(1.dp, extendedColors.border, shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = extendedColors.primary),
                onClick = onClick
            )
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = extendedColors.primary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = extendedColors.textPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp
                )
            )
        }
    }
}

@Composable
private fun QuickActionChip(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .testTag("quick_action_${label.lowercase().replace(" ", "_")}")
            .clip(shape)
            .background(extendedColors.surface)
            .border(1.dp, extendedColors.border, shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = extendedColors.primary),
                onClick = onClick
            )
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = extendedColors.energyColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = extendedColors.textPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp
                ),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ParticlePresetChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val extendedColors = StudyTheme.extendedColors
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) extendedColors.primary.copy(alpha = 0.2f) else extendedColors.surfaceHighlight)
            .border(
                1.dp,
                if (isSelected) extendedColors.borderHighlight else extendedColors.border,
                RoundedCornerShape(6.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isSelected) extendedColors.primary else extendedColors.textSecondary,
                fontSize = 10.sp
            )
        )
    }
}

@Composable
private fun AiStateItem(
    state: AiCompanionState,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val extendedColors = StudyTheme.extendedColors
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) extendedColors.borderHighlight else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            AiOrb(state = state, size = 40.dp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = state.label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isSelected) extendedColors.primary else extendedColors.textTertiary,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}
