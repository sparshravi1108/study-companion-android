package com.example.domain.dashboard.repository

import com.example.core.result.Resource
import com.example.domain.dashboard.model.DashboardData
import com.example.domain.dashboard.model.DashboardTask
import com.example.domain.dashboard.model.DashboardWidgetConfig
import com.example.domain.dashboard.model.TaskStatus
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun observeDashboardData(uid: String): Flow<Resource<DashboardData>>
    suspend fun updateTaskStatus(uid: String, taskId: String, newStatus: TaskStatus): Resource<Unit>
    suspend fun saveWidgetCustomization(uid: String, widgets: List<DashboardWidgetConfig>): Resource<Unit>
    suspend fun createTask(uid: String, task: DashboardTask): Resource<Unit>
    suspend fun seedSampleAcademicData(uid: String): Resource<Unit>
}
