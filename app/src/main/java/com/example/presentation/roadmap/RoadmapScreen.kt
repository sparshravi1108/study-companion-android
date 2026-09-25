package com.example.presentation.roadmap

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.presentation.components.EmptyStateView

@Composable
fun RoadmapScreen(
    onCreateRoadmap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("roadmap_screen")
            .background(MaterialTheme.colorScheme.background)
    ) {
        EmptyStateView(
            title = "Learning Roadmaps",
            description = "AI-generated study milestones, prerequisites, learning missions, and dynamic execution in Phase 4.",
            actionLabel = "Create Roadmap (Phase 4)",
            onActionClick = onCreateRoadmap
        )
    }
}
