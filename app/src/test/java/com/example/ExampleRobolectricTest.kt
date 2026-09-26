package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.core.dashboard.DashboardDateUtils
import com.example.domain.dashboard.model.DashboardTask
import com.example.domain.dashboard.model.DashboardWidgetId
import com.example.domain.dashboard.model.PriorityLevel
import com.example.domain.dashboard.model.TaskStatus
import com.example.domain.dashboard.model.UpcomingDeadline
import com.example.domain.dashboard.recommendation.DeterministicStudyRecommendationEngine
import com.example.domain.model.AnimationLevel
import com.example.domain.model.ThemeMode
import com.example.ui.theme.getExtendedColors
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Study Companion", appName)
    }

    @Test
    fun `verify seven themes are defined with valid color tokens`() {
        val allThemes = ThemeMode.entries
        assertEquals(7, allThemes.size)

        allThemes.forEach { mode ->
            val colors = getExtendedColors(mode)
            assertNotNull(colors.primary)
            assertNotNull(colors.glowStrong)
            assertNotNull(colors.background)
        }
    }

    @Test
    fun `verify animation level multipliers`() {
        assertEquals(0.6f, AnimationLevel.LOW.multiplier, 0.01f)
        assertEquals(1.0f, AnimationLevel.MEDIUM.multiplier, 0.01f)
        assertEquals(1.25f, AnimationLevel.HIGH.multiplier, 0.01f)
        assertEquals(1.5f, AnimationLevel.EXTREME.multiplier, 0.01f)
    }

    @Test
    fun `verify deterministic recommendation engine logic`() {
        val engine = DeterministicStudyRecommendationEngine()

        // Empty state
        val emptyRecommendation = engine.recommendNextAction(emptyList(), emptyList(), emptyList())
        assertNull(emptyRecommendation)

        // In-progress priority
        val inProgressTask = DashboardTask(
            id = "t1",
            title = "Kinematics",
            subjectName = "Physics",
            status = TaskStatus.IN_PROGRESS,
            priority = PriorityLevel.HIGH
        )
        val rec1 = engine.recommendNextAction(listOf(inProgressTask), emptyList(), emptyList())
        assertNotNull(rec1)
        assertTrue(rec1!!.reason.contains("In progress", ignoreCase = true))

        // Approaching deadline priority
        val deadline = UpcomingDeadline(
            id = "d1",
            title = "Math Exam",
            subjectName = "Mathematics",
            dueDateFormatted = "Tomorrow",
            priority = PriorityLevel.HIGH
        )
        val rec2 = engine.recommendNextAction(emptyList(), listOf(deadline), emptyList())
        assertNotNull(rec2)
        assertTrue(rec2!!.title.contains("Math Exam"))
    }

    @Test
    fun `verify eight dashboard widgets defined`() {
        val widgets = DashboardWidgetId.entries
        assertEquals(8, widgets.size)
    }

    @Test
    fun `verify dashboard greeting format`() {
        val morningGreeting = DashboardDateUtils.getGreeting(8)
        assertTrue(morningGreeting.contains("Morning"))

        val afternoonGreeting = DashboardDateUtils.getGreeting(14)
        assertTrue(afternoonGreeting.contains("Afternoon"))

        val eveningGreeting = DashboardDateUtils.getGreeting(19)
        assertTrue(eveningGreeting.contains("Evening"))
    }
}
