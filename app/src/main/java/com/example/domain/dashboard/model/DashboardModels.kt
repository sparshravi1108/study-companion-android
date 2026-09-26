package com.example.domain.dashboard.model

enum class TaskStatus(val label: String) {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    MISSED("Missed")
}

enum class PriorityLevel(val label: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    URGENT("Urgent")
}

data class DashboardTask(
    val id: String = "",
    val title: String = "",
    val type: String = "Practice", // Practice, Revision, Notes, Reading
    val subjectName: String? = null,
    val scheduledStart: String? = null, // e.g. "08:00"
    val scheduledEnd: String? = null,   // e.g. "08:45"
    val estimatedMinutes: Int = 30,
    val status: TaskStatus = TaskStatus.PENDING,
    val priority: PriorityLevel = PriorityLevel.MEDIUM,
    val dueTimestamp: Long = 0L,
    val isPriority: Boolean = false
)

data class UpcomingDeadline(
    val id: String = "",
    val title: String = "",
    val subjectName: String? = null,
    val dueTimestamp: Long = 0L,
    val dueDateFormatted: String = "",
    val priority: PriorityLevel = PriorityLevel.MEDIUM,
    val isOverdue: Boolean = false
)

data class RevisionItem(
    val id: String = "",
    val topic: String = "",
    val subjectName: String? = null,
    val dueTimestamp: Long = 0L,
    val dueText: String = "Due today",
    val estimatedMinutes: Int = 30,
    val priority: PriorityLevel = PriorityLevel.HIGH,
    val status: String = "Pending"
)

data class StudyRecommendation(
    val title: String,
    val subject: String,
    val reason: String,
    val priority: PriorityLevel,
    val estimatedMinutes: Int,
    val urgencyText: String,
    val sourceType: String = "Task",
    val sourceId: String = ""
)

data class ProgressMetrics(
    val completedTasks: Int = 0,
    val totalTasks: Int = 0,
    val completionPercentage: Int = 0,
    val remainingMinutes: Int = 0,
    val weeklyDayMinutes: List<Int> = listOf(45, 60, 30, 90, 45, 0, 0) // Mon - Sun
)

data class StudyStatistics(
    val totalStudyTimeFormatted: String = "0h 00m",
    val completedTasksCount: Int = 0,
    val streakDays: Int = 1,
    val totalTasksCount: Int = 0
)

enum class DashboardWidgetId(val title: String) {
    WHAT_SHOULD_I_STUDY("What Should You Study"),
    TODAYS_PLAN("Today's Plan"),
    CURRENT_PRIORITY("Current Priority"),
    UPCOMING_DEADLINES("Upcoming Deadlines"),
    REVISION_DUE("Revision Due"),
    PROGRESS_OVERVIEW("Progress Overview"),
    STUDY_STATISTICS("Study Statistics"),
    QUICK_ACTIONS("Quick Actions")
}

data class DashboardWidgetConfig(
    val widgetId: DashboardWidgetId,
    val visible: Boolean = true,
    val order: Int = 0
)

data class DashboardData(
    val recommendation: StudyRecommendation? = null,
    val todayTasks: List<DashboardTask> = emptyList(),
    val currentPriority: DashboardTask? = null,
    val upcomingDeadlines: List<UpcomingDeadline> = emptyList(),
    val revisionDue: List<RevisionItem> = emptyList(),
    val progress: ProgressMetrics = ProgressMetrics(),
    val statistics: StudyStatistics = StudyStatistics(),
    val widgetConfigs: List<DashboardWidgetConfig> = defaultWidgetConfigs()
) {
    val isEmpty: Boolean
        get() = todayTasks.isEmpty() && upcomingDeadlines.isEmpty() && revisionDue.isEmpty()

    companion object {
        fun defaultWidgetConfigs(): List<DashboardWidgetConfig> {
            return DashboardWidgetId.entries.mapIndexed { index, id ->
                DashboardWidgetConfig(
                    widgetId = id,
                    visible = true,
                    order = index
                )
            }
        }
    }
}
