package com.example.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.domain.model.AnimationLevel
import com.example.domain.model.ThemeMode
import com.example.ui.animation.AnimationConfig
import com.example.ui.animation.LocalAnimationConfig
import com.example.ui.animation.createAnimationConfig

val LocalExtendedColors = staticCompositionLocalOf {
    getExtendedColors(ThemeMode.INK_ANIME)
}

val LocalAnimationIntensity = staticCompositionLocalOf { 1.0f }
val LocalReduceMotion = staticCompositionLocalOf { false }

fun getExtendedColors(themeMode: ThemeMode): ExtendedStudyColors {
    return when (themeMode) {
        ThemeMode.LIGHT -> ExtendedStudyColors(
            primary = Color(0xFF2563EB),
            secondary = Color(0xFF4F46E5),
            accent = Color(0xFF0284C7),
            background = LightBg,
            surface = LightSurface,
            surfaceElevated = LightSurfaceElevated,
            surfaceHighlight = Color(0xFFE2E8F0),
            textPrimary = LightTextPrimary,
            textSecondary = LightTextSecondary,
            textTertiary = LightTextTertiary,
            border = LightBorder,
            borderHighlight = Color(0xFF2563EB).copy(alpha = 0.5f),
            glowSoft = Color(0xFF38BDF8).copy(alpha = 0.3f),
            glowStrong = Color(0xFF2563EB).copy(alpha = 0.6f),
            glowSuccess = SuccessGreen.copy(alpha = 0.5f),
            glowWarning = WarningAmber.copy(alpha = 0.5f),
            glowError = ErrorRed.copy(alpha = 0.5f),
            glowAi = Color(0xFF4F46E5),
            energyColor = Color(0xFF0284C7),
            accentGradient = LightGradient,
            backgroundGradient = Brush.verticalGradient(listOf(Color(0xFFF1F5F9), LightBg)),
            orbCore = Color(0xFF2563EB),
            orbRing = Color(0xFF0284C7)
        )
        ThemeMode.DARK -> ExtendedStudyColors(
            primary = Color(0xFF38BDF8),
            secondary = Color(0xFF818CF8),
            accent = Color(0xFF60A5FA),
            background = ClassicDarkBg,
            surface = ClassicDarkSurface,
            surfaceElevated = ClassicDarkElevated,
            surfaceHighlight = Color(0xFF334155),
            textPrimary = TextPrimary,
            textSecondary = TextSecondary,
            textTertiary = TextTertiary,
            border = ClassicDarkBorder,
            borderHighlight = Color(0xFF38BDF8).copy(alpha = 0.5f),
            glowSoft = Color(0xFF38BDF8).copy(alpha = 0.35f),
            glowStrong = Color(0xFF60A5FA).copy(alpha = 0.7f),
            glowSuccess = SuccessGreen,
            glowWarning = WarningAmber,
            glowError = ErrorRed,
            glowAi = Color(0xFF818CF8),
            energyColor = Color(0xFF38BDF8),
            accentGradient = ClassicDarkGradient,
            backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF0B1120), ClassicDarkBg)),
            orbCore = Color(0xFF38BDF8),
            orbRing = Color(0xFF818CF8)
        )
        ThemeMode.INK_ANIME -> ExtendedStudyColors(
            primary = NeonCyan,
            secondary = NeonPurple,
            accent = NeonPink,
            background = InkDarkVoid,
            surface = Color(0xFF0F172A),
            surfaceElevated = Color(0xFF1E293B),
            surfaceHighlight = Color(0xFF1E293B),
            textPrimary = TextPrimary,
            textSecondary = TextSecondary,
            textTertiary = TextTertiary,
            border = Color(0xFF1E293B),
            borderHighlight = NeonCyan.copy(alpha = 0.65f),
            glowSoft = NeonPurple.copy(alpha = 0.4f),
            glowStrong = NeonCyan.copy(alpha = 0.8f),
            glowSuccess = SuccessGreen,
            glowWarning = WarningAmber,
            glowError = ErrorRed,
            glowAi = NeonPurple,
            energyColor = NeonCyan,
            accentGradient = InkAnimeGradient,
            backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF04060B), InkDarkVoid)),
            orbCore = NeonCyan,
            orbRing = NeonPurple
        )
        ThemeMode.NEON_NIGHT -> ExtendedStudyColors(
            primary = Color(0xFFEC4899),
            secondary = Color(0xFF8B5CF6),
            accent = NeonCyan,
            background = Color(0xFF090614),
            surface = Color(0xFF110D20),
            surfaceElevated = Color(0xFF1C1434),
            surfaceHighlight = Color(0xFF281C48),
            textPrimary = TextPrimary,
            textSecondary = TextSecondary,
            textTertiary = TextTertiary,
            border = Color(0xFF281C48),
            borderHighlight = Color(0xFFEC4899).copy(alpha = 0.65f),
            glowSoft = Color(0xFF8B5CF6).copy(alpha = 0.4f),
            glowStrong = Color(0xFFEC4899).copy(alpha = 0.8f),
            glowSuccess = SuccessGreen,
            glowWarning = WarningAmber,
            glowError = ErrorRed,
            glowAi = Color(0xFF8B5CF6),
            energyColor = Color(0xFFEC4899),
            accentGradient = NeonNightGradient,
            backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF05030A), Color(0xFF090614))),
            orbCore = Color(0xFFEC4899),
            orbRing = Color(0xFF8B5CF6)
        )
        ThemeMode.CALM -> ExtendedStudyColors(
            primary = ZenEmerald,
            secondary = ZenTeal,
            accent = Color(0xFFA78BFA),
            background = Color(0xFF071112),
            surface = Color(0xFF0D1B1E),
            surfaceElevated = Color(0xFF14292D),
            surfaceHighlight = Color(0xFF1A3238),
            textPrimary = TextPrimary,
            textSecondary = TextSecondary,
            textTertiary = TextTertiary,
            border = Color(0xFF1A3238),
            borderHighlight = ZenEmerald.copy(alpha = 0.65f),
            glowSoft = ZenTeal.copy(alpha = 0.35f),
            glowStrong = ZenEmerald.copy(alpha = 0.75f),
            glowSuccess = ZenEmerald,
            glowWarning = WarningAmber,
            glowError = ErrorRed,
            glowAi = ZenTeal,
            energyColor = ZenEmerald,
            accentGradient = CalmGradient,
            backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF030809), Color(0xFF071112))),
            orbCore = ZenEmerald,
            orbRing = ZenTeal
        )
        ThemeMode.LIGHTNING -> ExtendedStudyColors(
            primary = LightningAmber,
            secondary = LightningGold,
            accent = NeonCyan,
            background = Color(0xFF0C0A07),
            surface = Color(0xFF191612),
            surfaceElevated = Color(0xFF26211A),
            surfaceHighlight = Color(0xFF3B2E18),
            textPrimary = TextPrimary,
            textSecondary = TextSecondary,
            textTertiary = TextTertiary,
            border = Color(0xFF3B2E18),
            borderHighlight = LightningAmber.copy(alpha = 0.65f),
            glowSoft = LightningGold.copy(alpha = 0.35f),
            glowStrong = LightningAmber.copy(alpha = 0.8f),
            glowSuccess = SuccessGreen,
            glowWarning = WarningAmber,
            glowError = ErrorRed,
            glowAi = LightningGold,
            energyColor = LightningAmber,
            accentGradient = LightningGradient,
            backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF070503), Color(0xFF0C0A07))),
            orbCore = LightningAmber,
            orbRing = LightningGold
        )
        ThemeMode.EXAM -> ExtendedStudyColors(
            primary = AcademicIndigo,
            secondary = AcademicSky,
            accent = Color(0xFF64748B),
            background = Color(0xFF080B12),
            surface = Color(0xFF0E1422),
            surfaceElevated = Color(0xFF161F34),
            surfaceHighlight = Color(0xFF1E2D4A),
            textPrimary = TextPrimary,
            textSecondary = TextSecondary,
            textTertiary = TextTertiary,
            border = Color(0xFF1E2D4A),
            borderHighlight = AcademicIndigo.copy(alpha = 0.65f),
            glowSoft = AcademicSky.copy(alpha = 0.35f),
            glowStrong = AcademicIndigo.copy(alpha = 0.75f),
            glowSuccess = SuccessGreen,
            glowWarning = WarningAmber,
            glowError = ErrorRed,
            glowAi = AcademicIndigo,
            energyColor = AcademicSky,
            accentGradient = ExamGradient,
            backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF04060A), Color(0xFF080B12))),
            orbCore = AcademicIndigo,
            orbRing = AcademicSky
        )
    }
}

fun getM3ColorScheme(themeMode: ThemeMode) = when (themeMode) {
    ThemeMode.LIGHT -> lightColorScheme(
        primary = Color(0xFF2563EB),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFDBEAFE),
        onPrimaryContainer = Color(0xFF1E3A8A),
        secondary = Color(0xFF4F46E5),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFE0E7FF),
        onSecondaryContainer = Color(0xFF312E81),
        tertiary = Color(0xFF0284C7),
        background = LightBg,
        onBackground = LightTextPrimary,
        surface = LightSurface,
        onSurface = LightTextPrimary,
        surfaceVariant = LightSurfaceElevated,
        onSurfaceVariant = LightTextSecondary,
        outline = LightBorder,
        error = ErrorRed,
        onError = Color.White
    )
    ThemeMode.DARK -> darkColorScheme(
        primary = Color(0xFF38BDF8),
        onPrimary = Color(0xFF003548),
        primaryContainer = Color(0xFF075985),
        onPrimaryContainer = Color(0xFFBAE6FD),
        secondary = Color(0xFF818CF8),
        onSecondary = Color(0xFF1E1B4B),
        secondaryContainer = Color(0xFF3730A3),
        onSecondaryContainer = Color(0xFFE0E7FF),
        tertiary = Color(0xFF34D399),
        background = ClassicDarkBg,
        onBackground = TextPrimary,
        surface = ClassicDarkSurface,
        onSurface = TextPrimary,
        surfaceVariant = ClassicDarkElevated,
        onSurfaceVariant = TextSecondary,
        outline = ClassicDarkBorder,
        error = ErrorRed,
        onError = Color.White
    )
    ThemeMode.INK_ANIME -> darkColorScheme(
        primary = NeonCyan,
        onPrimary = Color(0xFF04121A),
        primaryContainer = Color(0xFF00363D),
        onPrimaryContainer = Color(0xFF97F0FF),
        secondary = NeonPurple,
        onSecondary = Color(0xFF28034F),
        secondaryContainer = Color(0xFF451978),
        onSecondaryContainer = Color(0xFFE9D5FF),
        tertiary = LightningGold,
        background = InkDarkVoid,
        onBackground = TextPrimary,
        surface = InkCardSurface,
        onSurface = TextPrimary,
        surfaceVariant = Color(0xFF172033),
        onSurfaceVariant = TextSecondary,
        outline = InkCardBorder,
        error = ErrorRed,
        onError = Color.White
    )
    ThemeMode.NEON_NIGHT -> darkColorScheme(
        primary = Color(0xFFEC4899),
        onPrimary = Color(0xFF2D0017),
        primaryContainer = Color(0xFF5A0033),
        onPrimaryContainer = Color(0xFFFCE7F3),
        secondary = Color(0xFF8B5CF6),
        onSecondary = Color(0xFF1D004F),
        secondaryContainer = Color(0xFF451978),
        onSecondaryContainer = Color(0xFFEDE9FE),
        tertiary = Color(0xFF10B981),
        background = Color(0xFF090614),
        onBackground = TextPrimary,
        surface = Color(0xFF120E22),
        onSurface = TextPrimary,
        surfaceVariant = Color(0xFF1F1738),
        onSurfaceVariant = TextSecondary,
        outline = Color(0xFF33225C),
        error = ErrorRed,
        onError = Color.White
    )
    ThemeMode.CALM -> darkColorScheme(
        primary = ZenEmerald,
        onPrimary = Color(0xFF002115),
        primaryContainer = Color(0xFF00432E),
        onPrimaryContainer = Color(0xFFD1FAE5),
        secondary = ZenTeal,
        onSecondary = Color(0xFF002228),
        secondaryContainer = Color(0xFF084855),
        onSecondaryContainer = Color(0xFFCFFAFE),
        tertiary = Color(0xFFA78BFA),
        background = Color(0xFF071112),
        onBackground = TextPrimary,
        surface = Color(0xFF0D1C1E),
        onSurface = TextPrimary,
        surfaceVariant = Color(0xFF152A2D),
        onSurfaceVariant = TextSecondary,
        outline = Color(0xFF224348),
        error = ErrorRed,
        onError = Color.White
    )
    ThemeMode.LIGHTNING -> darkColorScheme(
        primary = LightningAmber,
        onPrimary = Color(0xFF2D1800),
        primaryContainer = Color(0xFF5A3200),
        onPrimaryContainer = Color(0xFFFEF3C7),
        secondary = LightningGold,
        onSecondary = Color(0xFF281C00),
        secondaryContainer = Color(0xFF4F3900),
        onSecondaryContainer = Color(0xFFFFFBEB),
        tertiary = Color(0xFF38BDF8),
        background = Color(0xFF0C0A07),
        onBackground = TextPrimary,
        surface = Color(0xFF18140D),
        onSurface = TextPrimary,
        surfaceVariant = Color(0xFF282114),
        onSurfaceVariant = TextSecondary,
        outline = Color(0xFF42371E),
        error = ErrorRed,
        onError = Color.White
    )
    ThemeMode.EXAM -> darkColorScheme(
        primary = AcademicIndigo,
        onPrimary = Color(0xFF001948),
        primaryContainer = Color(0xFF08328C),
        onPrimaryContainer = Color(0xFFDBEAFE),
        secondary = AcademicSky,
        onSecondary = Color(0xFF002235),
        secondaryContainer = Color(0xFF06476B),
        onSecondaryContainer = Color(0xFFE0F2FE),
        tertiary = Color(0xFF94A3B8),
        background = Color(0xFF080B12),
        onBackground = TextPrimary,
        surface = Color(0xFF0F1523),
        onSurface = TextPrimary,
        surfaceVariant = Color(0xFF192338),
        onSurfaceVariant = TextSecondary,
        outline = Color(0xFF263756),
        error = ErrorRed,
        onError = Color.White
    )
}

object StudyTheme {
    val extendedColors: ExtendedStudyColors
        @Composable
        @ReadOnlyComposable
        get() = LocalExtendedColors.current

    val animationConfig: AnimationConfig
        @Composable
        @ReadOnlyComposable
        get() = LocalAnimationConfig.current

    val animationIntensity: Float
        @Composable
        @ReadOnlyComposable
        get() = LocalAnimationIntensity.current

    val reduceMotion: Boolean
        @Composable
        @ReadOnlyComposable
        get() = LocalReduceMotion.current
}

@Composable
fun StudyCompanionTheme(
    themeMode: ThemeMode = ThemeMode.INK_ANIME,
    animationLevel: AnimationLevel = AnimationLevel.MEDIUM,
    animationIntensity: Float = 1.0f,
    reduceMotion: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = getM3ColorScheme(themeMode)
    val extendedColors = getExtendedColors(themeMode)
    val animationConfig = createAnimationConfig(
        level = animationLevel,
        reduceMotion = reduceMotion,
        customMultiplier = animationIntensity
    )

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors,
        LocalAnimationConfig provides animationConfig,
        LocalAnimationIntensity provides (if (reduceMotion) 0f else animationIntensity),
        LocalReduceMotion provides reduceMotion
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = StudyShapes,
            content = content
        )
    }
}

// Reusable Glow Modifier
fun Modifier.neonGlow(
    color: Color,
    radius: Dp = 12.dp,
    alpha: Float = 0.5f,
    shape: RoundedCornerShape = RoundedCornerShape(16.dp)
): Modifier = this.shadow(
    elevation = radius,
    shape = shape,
    spotColor = color.copy(alpha = alpha),
    ambientColor = color.copy(alpha = alpha * 0.5f)
)

fun Modifier.neonBorder(
    width: Dp = 1.dp,
    color: Color,
    shape: RoundedCornerShape = RoundedCornerShape(16.dp)
): Modifier = this.border(
    BorderStroke(width, color),
    shape = shape
)
