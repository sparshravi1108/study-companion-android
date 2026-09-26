package com.example.presentation.settings

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MotionPhotosOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.model.AnimationLevel
import com.example.domain.model.ThemeMode
import com.example.domain.model.UserProfile
import com.example.presentation.components.AnimationIntensitySelector
import com.example.presentation.components.GlowCard
import com.example.presentation.components.InkBackground
import com.example.presentation.components.ReduceMotionToggle
import com.example.presentation.components.ThemeSelectorSheet
import com.example.ui.animation.ParticlePreset
import com.example.ui.theme.StudySpacing
import com.example.ui.theme.StudyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userProfile: UserProfile,
    onBackClick: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onAppearanceChanged: (ThemeMode, AnimationLevel, Float, Boolean) -> Unit,
    onNavigateToCustomization: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showThemeSheet by remember { mutableStateOf(false) }
    var showIntensityDialog by remember { mutableStateOf(false) }

    val extendedColors = StudyTheme.extendedColors
    val currentPrefs = userProfile.preferences

    if (showThemeSheet) {
        ThemeSelectorSheet(
            currentTheme = currentPrefs.themeMode,
            currentLevel = currentPrefs.animationLevel,
            currentIntensity = currentPrefs.animationIntensity,
            currentReduceMotion = currentPrefs.reduceMotion,
            onDismiss = { showThemeSheet = false },
            onApplyTheme = { theme, level, intensity, reduceMotion ->
                onAppearanceChanged(theme, level, intensity, reduceMotion)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = extendedColors.textPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = extendedColors.textPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.testTag("settings_screen")
    ) { innerPadding ->
        InkBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            showParticles = false
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = StudySpacing.lg, vertical = StudySpacing.sm)
            ) {
                // User Profile Summary Tile
                GlowCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowEnabled = false,
                    onClick = onNavigateToProfile
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, extendedColors.primary, CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.hero_avatar),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userProfile.displayName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = extendedColors.textPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = userProfile.email,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = extendedColors.textSecondary
                                )
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "Open Profile",
                            tint = extendedColors.textTertiary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Appearance Section
                Text(
                    text = "Appearance",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = extendedColors.textPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                GlowCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowEnabled = false
                ) {
                    Column {
                        SettingsClickableRow(
                            icon = Icons.Default.Palette,
                            title = "Theme",
                            value = currentPrefs.themeMode.title,
                            onClick = { showThemeSheet = true }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        SettingsClickableRow(
                            icon = Icons.Default.Speed,
                            title = "Animation Intensity",
                            value = currentPrefs.animationLevel.label,
                            onClick = { showIntensityDialog = !showIntensityDialog }
                        )

                        if (showIntensityDialog && !currentPrefs.reduceMotion) {
                            Spacer(modifier = Modifier.height(10.dp))
                            AnimationIntensitySelector(
                                currentLevel = currentPrefs.animationLevel,
                                onLevelSelected = { newLevel ->
                                    onAppearanceChanged(
                                        currentPrefs.themeMode,
                                        newLevel,
                                        newLevel.multiplier,
                                        currentPrefs.reduceMotion
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        ReduceMotionToggle(
                            reduceMotion = currentPrefs.reduceMotion,
                            onToggle = { isReduced ->
                                onAppearanceChanged(
                                    currentPrefs.themeMode,
                                    currentPrefs.animationLevel,
                                    currentPrefs.animationIntensity,
                                    isReduced
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        SettingsClickableRow(
                            icon = Icons.Default.MotionPhotosOn,
                            title = "Theme Selector",
                            value = "7 Themes",
                            onClick = { showThemeSheet = true }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        SettingsClickableRow(
                            icon = Icons.Default.Palette,
                            title = "Dashboard Layout",
                            value = "Customize",
                            onClick = onNavigateToCustomization
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Notifications Section (Placeholder only as per instructions)
                Text(
                    text = "Notifications",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = extendedColors.textPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                GlowCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowEnabled = false
                ) {
                    SettingsClickableRow(
                        icon = Icons.Default.Notifications,
                        title = "Notification Settings",
                        value = "Phase 16",
                        onClick = { }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // General Section
                Text(
                    text = "General",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = extendedColors.textPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                GlowCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowEnabled = false
                ) {
                    Column {
                        SettingsClickableRow(
                            icon = Icons.Default.Info,
                            title = "About App",
                            value = "Study Companion v1.0",
                            onClick = { }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Phase 1 Visual System Active • Cloud-only architecture • No local database",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = extendedColors.textTertiary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun SettingsClickableRow(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    val extendedColors = StudyTheme.extendedColors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = extendedColors.primary),
                onClick = onClick
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = extendedColors.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = extendedColors.textPrimary,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                color = extendedColors.textSecondary
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = extendedColors.textTertiary,
            modifier = Modifier.size(14.dp)
        )
    }
}
