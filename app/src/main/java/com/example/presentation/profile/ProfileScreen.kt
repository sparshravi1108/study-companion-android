package com.example.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.domain.model.UserProfile
import com.example.presentation.components.AnimatedCard
import com.example.presentation.components.CyberOutlinedButton
import com.example.presentation.components.GlowCard
import com.example.presentation.components.InkBackground
import com.example.ui.animation.ParticlePreset
import com.example.ui.theme.LightningGold
import com.example.ui.theme.StudyRadii
import com.example.ui.theme.StudySpacing
import com.example.ui.theme.StudyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    onBackClick: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Profile",
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
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = extendedColors.textSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.testTag("profile_screen")
    ) { innerPadding ->
        InkBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            showParticles = true,
            particlePreset = ParticlePreset.SUBTLE
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = StudySpacing.lg, vertical = StudySpacing.sm),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar with glowing border and edit badge
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .border(
                            2.5.dp,
                            extendedColors.accentGradient,
                            CircleShape
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.hero_avatar),
                        contentDescription = "Profile Avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(extendedColors.surface)
                            .border(1.dp, extendedColors.primary, CircleShape)
                            .align(Alignment.BottomEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Avatar",
                            tint = extendedColors.primary,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = userProfile.displayName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = extendedColors.textPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = userProfile.email,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = extendedColors.textSecondary
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Gamification badges row using AnimatedCard
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnimatedCard(
                        modifier = Modifier.weight(1f),
                        delayMs = 50,
                        contentPadding = 10.dp
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = LightningGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Level ${userProfile.stats.level}",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = extendedColors.textPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            )
                            Text(
                                text = userProfile.stats.levelTitle,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = extendedColors.textTertiary,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    AnimatedCard(
                        modifier = Modifier.weight(1f),
                        delayMs = 100,
                        contentPadding = 10.dp
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = extendedColors.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${userProfile.stats.currentXp} XP",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = extendedColors.textPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            )
                            Text(
                                text = "Total XP",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = extendedColors.textTertiary,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    AnimatedCard(
                        modifier = Modifier.weight(1f),
                        delayMs = 150,
                        contentPadding = 10.dp
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = Color(0xFFF97316),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${userProfile.stats.streakDays}",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = extendedColors.textPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            )
                            Text(
                                text = "Day Streak",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = extendedColors.textTertiary,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Personal Info using GlowCard
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
                                text = "Personal Info",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = extendedColors.textPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "Edit >",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = extendedColors.primary,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.clickable { onNavigateToSettings() }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        ProfileInfoRow(
                            icon = Icons.Default.Email,
                            label = "Email",
                            value = userProfile.email
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        ProfileInfoRow(
                            icon = Icons.Default.Schedule,
                            label = "Timezone",
                            value = userProfile.timezone
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        ProfileInfoRow(
                            icon = Icons.Default.CalendarMonth,
                            label = "Joined",
                            value = userProfile.joinedDate
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        ProfileInfoRow(
                            icon = Icons.Default.Tune,
                            label = "Theme",
                            value = userProfile.preferences.themeMode.title
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Sign Out Button
                CyberOutlinedButton(
                    text = "Sign Out",
                    onClick = onSignOut,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Sign Out",
                            tint = Color(0xFFF87171),
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    testTag = "sign_out_button"
                )

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    val extendedColors = StudyTheme.extendedColors
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = extendedColors.primary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = extendedColors.textTertiary,
                    fontSize = 11.sp
                )
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = extendedColors.textPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            )
        }
    }
}
