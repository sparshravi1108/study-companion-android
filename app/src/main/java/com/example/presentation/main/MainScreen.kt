package com.example.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.AltRoute
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AiCompanionState
import com.example.domain.model.UserProfile
import com.example.navigation.BottomNavTab
import com.example.presentation.components.AiOrb
import com.example.presentation.dashboard.HomeDashboardScreen
import com.example.presentation.roadmap.RoadmapScreen
import com.example.presentation.subjects.SubjectsScreen
import com.example.ui.theme.StudyTheme
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun MainScreen(
    userProfile: UserProfile,
    isOnline: Boolean,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf<BottomNavTab>(BottomNavTab.Home) }
    var floatingAiState by remember { mutableStateOf(AiCompanionState.IDLE) }
    var showFloatingAiDialog by remember { mutableStateOf(false) }

    val extendedColors = StudyTheme.extendedColors

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF090D18))
                    .border(
                        width = 1.dp,
                        color = Color(0xFF161F33),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomNavItem(
                        title = "Home",
                        selected = selectedTab == BottomNavTab.Home,
                        activeIcon = Icons.Default.Home,
                        inactiveIcon = Icons.Outlined.Home,
                        onClick = { selectedTab = BottomNavTab.Home }
                    )

                    BottomNavItem(
                        title = "Subjects",
                        selected = selectedTab == BottomNavTab.Subjects,
                        activeIcon = Icons.AutoMirrored.Filled.MenuBook,
                        inactiveIcon = Icons.AutoMirrored.Outlined.MenuBook,
                        onClick = { selectedTab = BottomNavTab.Subjects }
                    )

                    BottomNavItem(
                        title = "Roadmap",
                        selected = selectedTab == BottomNavTab.Roadmap,
                        activeIcon = Icons.Default.AltRoute,
                        inactiveIcon = Icons.Outlined.AltRoute,
                        onClick = { selectedTab = BottomNavTab.Roadmap }
                    )

                    BottomNavItem(
                        title = "More",
                        selected = selectedTab == BottomNavTab.More,
                        activeIcon = Icons.Default.MoreHoriz,
                        inactiveIcon = Icons.Default.MoreHoriz,
                        onClick = {
                            selectedTab = BottomNavTab.More
                            onNavigateToSettings()
                        }
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.testTag("main_scaffold")
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                BottomNavTab.Home -> {
                    HomeDashboardScreen(
                        userProfile = userProfile,
                        isOnline = isOnline,
                        onNavigateToSubjects = { selectedTab = BottomNavTab.Subjects },
                        onNavigateToRoadmap = { selectedTab = BottomNavTab.Roadmap },
                        onNavigateToProfile = onNavigateToProfile,
                        onNavigateToSettings = onNavigateToSettings
                    )
                }
                BottomNavTab.Subjects -> {
                    SubjectsScreen(
                        onAddSubject = { selectedTab = BottomNavTab.Home }
                    )
                }
                BottomNavTab.Roadmap -> {
                    RoadmapScreen(
                        onCreateRoadmap = { selectedTab = BottomNavTab.Home }
                    )
                }
                BottomNavTab.More,
                BottomNavTab.Profile,
                BottomNavTab.Settings -> {
                    HomeDashboardScreen(
                        userProfile = userProfile,
                        isOnline = isOnline,
                        onNavigateToSubjects = { selectedTab = BottomNavTab.Subjects },
                        onNavigateToRoadmap = { selectedTab = BottomNavTab.Roadmap },
                        onNavigateToProfile = onNavigateToProfile,
                        onNavigateToSettings = onNavigateToSettings
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    title: String,
    selected: Boolean,
    activeIcon: ImageVector,
    inactiveIcon: ImageVector,
    onClick: () -> Unit
) {
    val extendedColors = StudyTheme.extendedColors
    val activeColor = extendedColors.glowColor

    Box(
        modifier = Modifier
            .testTag("bottom_nav_tab_${title.lowercase()}")
            .size(width = 68.dp, height = 54.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = activeColor),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (selected) activeIcon else inactiveIcon,
                contentDescription = title,
                tint = if (selected) activeColor else TextTertiary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = if (selected) TextPrimary else TextTertiary,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 10.sp
                )
            )
        }
    }
}
