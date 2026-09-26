package com.example.presentation.dashboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.model.AiCompanionState
import com.example.presentation.components.AiOrb
import com.example.presentation.components.EnergyProgressBar
import com.example.presentation.components.InkBackground
import com.example.presentation.components.NeonButton
import com.example.ui.animation.ParticlePreset
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.StudySpacing
import com.example.ui.theme.StudyTheme

@Composable
fun DashboardLoadingState(
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors

    InkBackground(
        modifier = modifier.fillMaxSize().testTag("dashboard_loading_state"),
        showParticles = true,
        particlePreset = ParticlePreset.SUBTLE
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(StudySpacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(extendedColors.surface)
                    .border(2.dp, extendedColors.accentGradient, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AiOrb(
                    state = AiCompanionState.THINKING,
                    size = 96.dp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Loading your dashboard...",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = extendedColors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Gathering your academic data",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = extendedColors.textSecondary,
                    fontSize = 13.sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.width(220.dp)) {
                EnergyProgressBar(
                    progress = 0.65f,
                    height = 6.dp
                )
            }
        }
    }
}

@Composable
fun DashboardEmptyState(
    onAddFirstTask: () -> Unit,
    onExploreFeatures: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors

    InkBackground(
        modifier = modifier.fillMaxSize().testTag("dashboard_empty_state"),
        showParticles = true,
        particlePreset = ParticlePreset.ENERGY
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(StudySpacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .border(2.5.dp, extendedColors.accentGradient, CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hero_avatar),
                    contentDescription = "Cosmic Illustration",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Your academic command\ncenter is ready!",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = extendedColors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Add your first task, deadline or study item to get started.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = extendedColors.textSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            NeonButton(
                text = "Add Your First Task",
                onClick = onAddFirstTask,
                modifier = Modifier.fillMaxWidth(0.75f),
                height = 46.dp,
                testTag = "add_first_task_button"
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Explore Features",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = extendedColors.primary,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier
                    .clickable(onClick = onExploreFeatures)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun DashboardOfflineState(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors

    InkBackground(
        modifier = modifier.fillMaxSize().testTag("dashboard_offline_state"),
        showParticles = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(StudySpacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(extendedColors.surfaceHighlight)
                    .border(1.5.dp, extendedColors.borderHighlight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WifiOff,
                    contentDescription = "Offline",
                    tint = extendedColors.primary,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "You're offline",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = extendedColors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Connect to the internet to load your latest academic data.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = extendedColors.textSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            NeonButton(
                text = "Try Again",
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(0.7f),
                height = 46.dp,
                testTag = "offline_try_again_button"
            )
        }
    }
}

@Composable
fun DashboardErrorState(
    errorMessage: String,
    onRetry: () -> Unit,
    onGoHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors

    InkBackground(
        modifier = modifier.fillMaxSize().testTag("dashboard_error_state"),
        showParticles = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(StudySpacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(ErrorRed.copy(alpha = 0.15f))
                    .border(1.5.dp, ErrorRed.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ErrorOutline,
                    contentDescription = "Error",
                    tint = ErrorRed,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Something went wrong",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = extendedColors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We couldn't load your dashboard. Please try again.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = extendedColors.textSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            NeonButton(
                text = "Retry",
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(0.7f),
                height = 46.dp,
                testTag = "error_retry_button"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Go to Home",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = extendedColors.textTertiary,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .clickable(onClick = onGoHome)
                    .padding(8.dp)
            )
        }
    }
}
