package com.example.presentation.subjects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.presentation.components.EmptyStateView

@Composable
fun SubjectsScreen(
    onAddSubject: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("subjects_screen")
            .background(MaterialTheme.colorScheme.background)
    ) {
        EmptyStateView(
            title = "Subjects & Syllabus",
            description = "Manage academic units, topics, mastery levels, and AI syllabus imports in Phase 3.",
            actionLabel = "Add Subject (Phase 3)",
            onActionClick = onAddSubject
        )
    }
}
