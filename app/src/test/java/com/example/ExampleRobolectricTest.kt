package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.domain.model.AnimationLevel
import com.example.domain.model.ThemeMode
import com.example.ui.theme.getExtendedColors
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
}
