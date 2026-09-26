package com.example.core.dashboard

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DashboardDateUtils {

    fun getGreeting(hourOfDay: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)): String {
        return when (hourOfDay) {
            in 5..11 -> "Good Morning ☀️"
            in 12..16 -> "Good Afternoon 🌤️"
            in 17..21 -> "Good Evening 🌙"
            else -> "Good Night 🌌"
        }
    }

    fun getTodayFormatted(): String {
        val sdf = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    fun formatDeadline(dueTimestamp: Long): Pair<String, Boolean> {
        val now = System.currentTimeMillis()
        if (dueTimestamp <= 0L) return Pair("No due date", false)

        val diffMs = dueTimestamp - now
        val isOverdue = diffMs < 0

        val calDue = Calendar.getInstance().apply { timeInMillis = dueTimestamp }
        val calNow = Calendar.getInstance().apply { timeInMillis = now }

        val isSameDay = calDue.get(Calendar.YEAR) == calNow.get(Calendar.YEAR) &&
                calDue.get(Calendar.DAY_OF_YEAR) == calNow.get(Calendar.DAY_OF_YEAR)

        val calTomorrow = Calendar.getInstance().apply {
            timeInMillis = now
            add(Calendar.DAY_OF_YEAR, 1)
        }
        val isTomorrow = calDue.get(Calendar.YEAR) == calTomorrow.get(Calendar.YEAR) &&
                calDue.get(Calendar.DAY_OF_YEAR) == calTomorrow.get(Calendar.DAY_OF_YEAR)

        val timeSdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        val timeStr = timeSdf.format(Date(dueTimestamp))

        val text = when {
            isOverdue -> {
                val overdueDays = (Math.abs(diffMs) / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(1)
                if (overdueDays == 1) "Overdue by 1 day" else "Overdue by $overdueDays days"
            }
            isSameDay -> "Today • $timeStr"
            isTomorrow -> "Tomorrow • $timeStr"
            diffMs < 7 * 24 * 60 * 60 * 1000L -> {
                val days = (diffMs / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(2)
                "In $days days"
            }
            else -> "Next week"
        }

        return Pair(text, isOverdue)
    }
}
