package com.example.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.network.NetworkMonitor
import com.example.core.result.Resource
import com.example.domain.dashboard.model.DashboardData
import com.example.domain.dashboard.model.DashboardTask
import com.example.domain.dashboard.model.DashboardWidgetConfig
import com.example.domain.dashboard.model.TaskStatus
import com.example.domain.dashboard.repository.DashboardRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val dashboardRepository: DashboardRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private var currentUid: String = ""
    private var dataCollectionJob: Job? = null

    init {
        viewModelScope.launch {
            networkMonitor.isOnline.collectLatest { online ->
                if (!online && _uiState.value is DashboardUiState.Loading) {
                    _uiState.value = DashboardUiState.Offline
                }
            }
        }
    }

    fun initialize(uid: String) {
        if (currentUid == uid && _uiState.value !is DashboardUiState.Loading && _uiState.value !is DashboardUiState.Error) {
            return
        }
        currentUid = uid
        startDataCollection(uid)
    }

    fun refresh() {
        if (currentUid.isNotBlank()) {
            val currentState = _uiState.value
            if (currentState is DashboardUiState.Content) {
                _uiState.value = currentState.copy(isRefreshing = true)
            }
            startDataCollection(currentUid)
        }
    }

    private fun startDataCollection(uid: String) {
        dataCollectionJob?.cancel()
        dataCollectionJob = viewModelScope.launch {
            if (!networkMonitor.isCurrentlyOnline()) {
                _uiState.value = DashboardUiState.Offline
                return@launch
            }

            dashboardRepository.observeDashboardData(uid).collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        if (_uiState.value !is DashboardUiState.Content) {
                            _uiState.value = DashboardUiState.Loading
                        }
                    }
                    is Resource.Success -> {
                        val data = resource.data
                        if (data.isEmpty) {
                            _uiState.value = DashboardUiState.Empty(isOnline = networkMonitor.isCurrentlyOnline())
                        } else {
                            _uiState.value = DashboardUiState.Content(data = data, isRefreshing = false)
                        }
                    }
                    is Resource.Error -> {
                        _uiState.value = DashboardUiState.Error(resource.message ?: "Failed to load academic dashboard")
                    }
                    is Resource.Idle -> {
                        // no-op
                    }
                }
            }
        }
    }

    fun toggleTaskStatus(taskId: String) {
        val currentState = _uiState.value
        if (currentState !is DashboardUiState.Content) return

        val task = currentState.data.todayTasks.find { it.id == taskId } ?: return
        val newStatus = if (task.status == TaskStatus.COMPLETED) TaskStatus.PENDING else TaskStatus.COMPLETED

        viewModelScope.launch {
            dashboardRepository.updateTaskStatus(currentUid, taskId, newStatus)
        }
    }

    fun seedStarterData() {
        if (currentUid.isBlank()) return
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            dashboardRepository.seedSampleAcademicData(currentUid)
        }
    }

    fun saveCustomization(configs: List<DashboardWidgetConfig>) {
        if (currentUid.isBlank()) return
        viewModelScope.launch {
            dashboardRepository.saveWidgetCustomization(currentUid, configs)
        }
    }
}
