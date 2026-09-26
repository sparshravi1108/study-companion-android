package com.example.presentation.dashboard

import com.example.domain.dashboard.model.DashboardData

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Content(
        val data: DashboardData,
        val isRefreshing: Boolean = false
    ) : DashboardUiState
    data class Empty(
        val isOnline: Boolean = true
    ) : DashboardUiState
    data object Offline : DashboardUiState
    data class Error(
        val message: String
    ) : DashboardUiState
}
