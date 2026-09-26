package com.example.domain.dashboard.recommendation

import com.example.domain.dashboard.model.DashboardTask
import com.example.domain.dashboard.model.PriorityLevel
import com.example.domain.dashboard.model.RevisionItem
import com.example.domain.dashboard.model.StudyRecommendation
import com.example.domain.dashboard.model.TaskStatus
import com.example.domain.dashboard.model.UpcomingDeadline

interface StudyRecommendationEngine {
    fun recommendNextAction(
        tasks: List<DashboardTask>,
        deadlines: List<UpcomingDeadline>,
        revisions: List<RevisionItem>
    ): StudyRecommendation?
}

class DeterministicStudyRecommendationEngine : StudyRecommendationEngine {

    override fun recommendNextAction(
        tasks: List<DashboardTask>,
        deadlines: List<UpcomingDeadline>,
        revisions: List<RevisionItem>
    ): StudyRecommendation? {
        // 1. Check for in-progress tasks
        val inProgressTask = tasks.firstOrNull { it.status == TaskStatus.IN_PROGRESS }
        if (inProgressTask != null) {
            return StudyRecommendation(
                title = "${inProgressTask.subjectName ?: "Study"} — ${inProgressTask.title}",
                subject = inProgressTask.subjectName ?: "General",
                reason = "Currently in progress. Continue to stay on track with today's plan.",
                priority = inProgressTask.priority,
                estimatedMinutes = inProgressTask.estimatedMinutes,
                urgencyText = "In progress • ${inProgressTask.priority.label} priority • ${inProgressTask.estimatedMinutes} min",
                sourceType = "Task",
                sourceId = inProgressTask.id
            )
        }

        // 2. Check for overdue deadlines
        val overdueDeadline = deadlines.firstOrNull { it.isOverdue }
        if (overdueDeadline != null) {
            return StudyRecommendation(
                title = "${overdueDeadline.subjectName ?: "Urgent"} — ${overdueDeadline.title}",
                subject = overdueDeadline.subjectName ?: "General",
                reason = "Past due date. Resolve immediately to prevent academic backlog.",
                priority = PriorityLevel.URGENT,
                estimatedMinutes = 45,
                urgencyText = "Overdue • Urgent priority • 45 min",
                sourceType = "Deadline",
                sourceId = overdueDeadline.id
            )
        }

        // 3. High priority pending tasks for today
        val highPriorityPending = tasks.firstOrNull {
            it.status == TaskStatus.PENDING && (it.priority == PriorityLevel.HIGH || it.priority == PriorityLevel.URGENT)
        }
        if (highPriorityPending != null) {
            return StudyRecommendation(
                title = "${highPriorityPending.subjectName ?: "Study"} — ${highPriorityPending.title}",
                subject = highPriorityPending.subjectName ?: "General",
                reason = "Because it's due today and marked as high priority in your schedule.",
                priority = highPriorityPending.priority,
                estimatedMinutes = highPriorityPending.estimatedMinutes,
                urgencyText = "Due today • ${highPriorityPending.priority.label} priority • ${highPriorityPending.estimatedMinutes} min",
                sourceType = "Task",
                sourceId = highPriorityPending.id
            )
        }

        // 4. Revision due today
        val revisionToday = revisions.firstOrNull { it.dueText.contains("today", ignoreCase = true) }
        if (revisionToday != null) {
            return StudyRecommendation(
                title = "${revisionToday.subjectName ?: "Revision"} — ${revisionToday.topic}",
                subject = revisionToday.subjectName ?: "General",
                reason = "Scheduled for revision today to solidify retention.",
                priority = revisionToday.priority,
                estimatedMinutes = revisionToday.estimatedMinutes,
                urgencyText = "Due today • ${revisionToday.priority.label} priority • ${revisionToday.estimatedMinutes} min",
                sourceType = "Revision",
                sourceId = revisionToday.id
            )
        }

        // 5. Approaching deadline within 24-48 hours
        val approachingDeadline = deadlines.firstOrNull()
        if (approachingDeadline != null) {
            return StudyRecommendation(
                title = "${approachingDeadline.subjectName ?: "Upcoming"} — ${approachingDeadline.title}",
                subject = approachingDeadline.subjectName ?: "General",
                reason = "Deadline approaching (${approachingDeadline.dueDateFormatted}). Early preparation prevents last-minute cramming.",
                priority = approachingDeadline.priority,
                estimatedMinutes = 45,
                urgencyText = "${approachingDeadline.dueDateFormatted} • ${approachingDeadline.priority.label} priority • 45 min",
                sourceType = "Deadline",
                sourceId = approachingDeadline.id
            )
        }

        // 6. Next pending task in today's plan
        val nextPending = tasks.firstOrNull { it.status == TaskStatus.PENDING }
        if (nextPending != null) {
            return StudyRecommendation(
                title = "${nextPending.subjectName ?: "Study"} — ${nextPending.title}",
                subject = nextPending.subjectName ?: "General",
                reason = "Next planned item in your daily schedule.",
                priority = nextPending.priority,
                estimatedMinutes = nextPending.estimatedMinutes,
                urgencyText = "Scheduled • ${nextPending.priority.label} priority • ${nextPending.estimatedMinutes} min",
                sourceType = "Task",
                sourceId = nextPending.id
            )
        }

        return null
    }
}
