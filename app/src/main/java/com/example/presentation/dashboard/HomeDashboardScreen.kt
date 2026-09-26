package com.example.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.dashboard.model.DashboardWidgetId
import com.example.domain.model.AiCompanionState
import com.example.domain.model.UserProfile
import com.example.presentation.components.InkBackground
import com.example.presentation.dashboard.components.AddTaskBottomSheet
import com.example.presentation.dashboard.components.CurrentPriorityCard
import com.example.presentation.dashboard.components.DashboardEmptyState
import com.example.presentation.dashboard.components.DashboardErrorState
import com.example.presentation.dashboard.components.DashboardHeader
import com.example.presentation.dashboard.components.DashboardLoadingState
import com.example.presentation.dashboard.components.DashboardOfflineState
import com.example.presentation.dashboard.components.ProgressOverviewCard
import com.example.presentation.dashboard.components.QuickActionsSection
import com.example.presentation.dashboard.components.RevisionDueCard
import com.example.presentation.dashboard.components.StudyStatisticsCard
import com.example.presentation.dashboard.components.TodayPlanCard
import com.example.presentation.dashboard.components.UpcomingDeadlinesCard
import com.example.presentation.dashboard.components.WhatShouldIStudyCard
import com.example.ui.animation.ParticlePreset
import com.example.ui.theme.StudySpacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDashboardScreen(
    userProfile: UserProfile,
    viewModel: DashboardViewModel,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToCustomization: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showAddTaskDialog by remember { mutableStateOf(false) }

    LaunchedEffect(userProfile.uid) {
        if (userProfile.uid.isNotBlank()) {
            viewModel.initialize(userProfile.uid)
        }
    }

    if (showAddTaskDialog) {
        AddTaskBottomSheet(
            onDismiss = { showAddTaskDialog = false },
            onSaveTask = { newTask ->
                scope.launch {
                    viewModel.toggleTaskStatus("") // trigger
                }
            }
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is DashboardUiState.Loading -> {
                DashboardLoadingState()
            }
            is DashboardUiState.Offline -> {
                DashboardOfflineState(
                    onRetry = { viewModel.refresh() }
                )
            }
            is DashboardUiState.Error -> {
                DashboardErrorState(
                    errorMessage = state.message,
                    onRetry = { viewModel.refresh() },
                    onGoHome = { viewModel.refresh() }
                )
            }
            is DashboardUiState.Empty -> {
                DashboardEmptyState(
                    onAddFirstTask = { viewModel.seedStarterData() },
                    onExploreFeatures = { viewModel.seedStarterData() }
                )
            }
            is DashboardUiState.Content -> {
                val data = state.data
                val widgetConfigs = data.widgetConfigs.sortedBy { it.order }

                InkBackground(
                    modifier = Modifier.fillMaxSize(),
                    showParticles = true,
                    particlePreset = ParticlePreset.SUBTLE
                ) {
                    PullToRefreshBox(
                        isRefreshing = state.isRefreshing,
                        onRefresh = { viewModel.refresh() },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            state = rememberLazyListState(),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = StudySpacing.lg),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                Spacer(modifier = Modifier.height(StudySpacing.sm))
                                DashboardHeader(
                                    userProfile = userProfile,
                                    aiState = AiCompanionState.IDLE,
                                    onAvatarClick = onNavigateToProfile,
                                    onCustomizationClick = onNavigateToCustomization
                                )
                            }

                            widgetConfigs.forEach { config ->
                                if (config.visible) {
                                    item(key = config.widgetId.name) {
                                        when (config.widgetId) {
                                            DashboardWidgetId.WHAT_SHOULD_I_STUDY -> {
                                                WhatShouldIStudyCard(
                                                    recommendation = data.recommendation,
                                                    onActionClick = {
                                                        scope.launch {
                                                            snackbarHostState.showSnackbar("Opening recommended focus session")
                                                        }
                                                    }
                                                )
                                            }
                                            DashboardWidgetId.TODAYS_PLAN -> {
                                                TodayPlanCard(
                                                    tasks = data.todayTasks,
                                                    progress = data.progress,
                                                    onTaskClick = { taskId ->
                                                        viewModel.toggleTaskStatus(taskId)
                                                    },
                                                    onViewAllClick = {
                                                        scope.launch {
                                                            snackbarHostState.showSnackbar("All today's tasks displayed")
                                                        }
                                                    }
                                                )
                                            }
                                            DashboardWidgetId.CURRENT_PRIORITY -> {
                                                BoxWithConstraints {
                                                    if (maxWidth > 600.dp && widgetConfigs.any { it.widgetId == DashboardWidgetId.UPCOMING_DEADLINES && it.visible }) {
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                                                        ) {
                                                            CurrentPriorityCard(
                                                                task = data.currentPriority,
                                                                onOpenClick = {
                                                                    scope.launch {
                                                                        snackbarHostState.showSnackbar("Opening priority task")
                                                                    }
                                                                },
                                                                modifier = Modifier.weight(1f)
                                                            )
                                                            UpcomingDeadlinesCard(
                                                                deadlines = data.upcomingDeadlines,
                                                                onViewAllClick = {
                                                                    scope.launch {
                                                                        snackbarHostState.showSnackbar("All upcoming deadlines displayed")
                                                                    }
                                                                },
                                                                modifier = Modifier.weight(1f)
                                                            )
                                                        }
                                                    } else {
                                                        CurrentPriorityCard(
                                                            task = data.currentPriority,
                                                            onOpenClick = {
                                                                scope.launch {
                                                                    snackbarHostState.showSnackbar("Opening priority task")
                                                                }
                                                            }
                                                        )
                                                    }
                                                }
                                            }
                                            DashboardWidgetId.UPCOMING_DEADLINES -> {
                                                BoxWithConstraints {
                                                    if (maxWidth <= 600.dp) {
                                                        UpcomingDeadlinesCard(
                                                            deadlines = data.upcomingDeadlines,
                                                            onViewAllClick = {
                                                                scope.launch {
                                                                    snackbarHostState.showSnackbar("All upcoming deadlines displayed")
                                                                }
                                                            }
                                                        )
                                                    }
                                                }
                                            }
                                            DashboardWidgetId.REVISION_DUE -> {
                                                BoxWithConstraints {
                                                    if (maxWidth > 600.dp && widgetConfigs.any { it.widgetId == DashboardWidgetId.PROGRESS_OVERVIEW && it.visible }) {
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                                                        ) {
                                                            RevisionDueCard(
                                                                revisions = data.revisionDue,
                                                                onViewAllClick = {
                                                                    scope.launch {
                                                                        snackbarHostState.showSnackbar("All revision items displayed")
                                                                    }
                                                                },
                                                                modifier = Modifier.weight(1f)
                                                            )
                                                            ProgressOverviewCard(
                                                                progress = data.progress,
                                                                modifier = Modifier.weight(1f)
                                                            )
                                                        }
                                                    } else {
                                                        RevisionDueCard(
                                                            revisions = data.revisionDue,
                                                            onViewAllClick = {
                                                                scope.launch {
                                                                    snackbarHostState.showSnackbar("All revision items displayed")
                                                                }
                                                            }
                                                        )
                                                    }
                                                }
                                            }
                                            DashboardWidgetId.PROGRESS_OVERVIEW -> {
                                                BoxWithConstraints {
                                                    if (maxWidth <= 600.dp) {
                                                        ProgressOverviewCard(progress = data.progress)
                                                    }
                                                }
                                            }
                                            DashboardWidgetId.STUDY_STATISTICS -> {
                                                StudyStatisticsCard(statistics = data.statistics)
                                            }
                                            DashboardWidgetId.QUICK_ACTIONS -> {
                                                QuickActionsSection(
                                                    onStartFocus = {
                                                        scope.launch {
                                                            snackbarHostState.showSnackbar("Focus session will be available in Phase 15")
                                                        }
                                                    },
                                                    onAddTask = {
                                                        showAddTaskDialog = true
                                                    },
                                                    onAddSubject = {
                                                        scope.launch {
                                                            snackbarHostState.showSnackbar("Subject Manager will be available in Phase 3")
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(28.dp))
                            }
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}
