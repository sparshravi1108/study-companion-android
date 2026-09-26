package com.example.presentation.dashboard.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.core.dashboard.DashboardDateUtils
import com.example.domain.model.AiCompanionState
import com.example.domain.model.UserProfile
import com.example.presentation.components.AiOrb
import com.example.ui.theme.StudyTheme

@Composable
fun DashboardHeader(
    userProfile: UserProfile,
    aiState: AiCompanionState,
    onAvatarClick: () -> Unit,
    onCustomizationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors
    val greeting = DashboardDateUtils.getGreeting()
    val todayDate = DashboardDateUtils.getTodayFormatted()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag("dashboard_header"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Profile avatar with glowing neon ring
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .border(2.dp, extendedColors.accentGradient, CircleShape)
                    .clickable(onClick = onAvatarClick)
                    .testTag("header_avatar"),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hero_avatar),
                    contentDescription = "User Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = greeting,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = extendedColors.textSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = userProfile.displayName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = extendedColors.textPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    maxLines = 1
                )
                Text(
                    text = todayDate,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = extendedColors.textTertiary,
                        fontSize = 11.sp
                    )
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onCustomizationClick,
                modifier = Modifier.testTag("header_customize_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Customize Dashboard Layout",
                    tint = extendedColors.textSecondary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            // AI Orb visual companion
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(extendedColors.surface)
                    .border(1.2.dp, extendedColors.borderHighlight.copy(alpha = 0.6f), CircleShape)
                    .testTag("header_ai_orb"),
                contentAlignment = Alignment.Center
            ) {
                AiOrb(
                    state = aiState,
                    size = 48.dp,
                    onClick = onCustomizationClick
                )
            }
        }
    }
}
