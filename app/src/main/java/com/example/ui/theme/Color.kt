package com.example.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Core Signature Colors
val NeonCyan = Color(0xFF00F0FF)
val NeonPurple = Color(0xFF8B5CF6)
val NeonPink = Color(0xFFEC4899)
val ElectricBlue = Color(0xFF3B82F6)
val LightningGold = Color(0xFFFBBF24)
val LightningAmber = Color(0xFFF59E0B)
val SakuraPink = Color(0xFFF43F5E)
val ZenEmerald = Color(0xFF10B981)
val ZenTeal = Color(0xFF06B6D4)
val AcademicIndigo = Color(0xFF2563EB)
val AcademicSky = Color(0xFF38BDF8)

val SuccessGreen = Color(0xFF10B981)
val WarningAmber = Color(0xFFF59E0B)
val ErrorRed = Color(0xFFEF4444)

// Dark/Void Surfaces
val InkDarkVoid = Color(0xFF080C16)
val InkDarkNavy = Color(0xFF0D1322)
val InkCardSurface = Color(0xFF131B2E)
val InkCardBorder = Color(0xFF1E2B48)
val InkCardElevated = Color(0xFF19233C)

// Classic Dark Surfaces
val ClassicDarkBg = Color(0xFF0F172A)
val ClassicDarkSurface = Color(0xFF1E293B)
val ClassicDarkElevated = Color(0xFF334155)
val ClassicDarkBorder = Color(0xFF334155)

// Light Theme Surfaces
val LightBg = Color(0xFFF8FAFC)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceElevated = Color(0xFFF1F5F9)
val LightBorder = Color(0xFFE2E8F0)
val LightTextPrimary = Color(0xFF0F172A)
val LightTextSecondary = Color(0xFF475569)
val LightTextTertiary = Color(0xFF94A3B8)

// Neutral Text Colors
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextTertiary = Color(0xFF64748B)

// Gradients
val InkAnimeGradient = Brush.linearGradient(
    listOf(NeonCyan, NeonPurple)
)

val NeonNightGradient = Brush.linearGradient(
    listOf(Color(0xFF8B5CF6), Color(0xFFEC4899))
)

val CalmGradient = Brush.linearGradient(
    listOf(ZenEmerald, ZenTeal)
)

val LightningGradient = Brush.linearGradient(
    listOf(LightningAmber, LightningGold)
)

val ExamGradient = Brush.linearGradient(
    listOf(AcademicIndigo, AcademicSky)
)

val LightGradient = Brush.linearGradient(
    listOf(Color(0xFF2563EB), Color(0xFF4F46E5))
)

val ClassicDarkGradient = Brush.linearGradient(
    listOf(Color(0xFF38BDF8), Color(0xFF6366F1))
)

@Immutable
data class ExtendedStudyColors(
    val primary: Color,
    val secondary: Color,
    val accent: Color,
    val background: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val surfaceHighlight: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val border: Color,
    val borderHighlight: Color,
    val glowSoft: Color,
    val glowStrong: Color,
    val glowSuccess: Color,
    val glowWarning: Color,
    val glowError: Color,
    val glowAi: Color,
    val energyColor: Color,
    val accentGradient: Brush,
    val backgroundGradient: Brush,
    val orbCore: Color,
    val orbRing: Color,
    // Backward compatibility aliases
    val glowColor: Color = glowStrong,
    val secondaryGlow: Color = glowSoft,
    val cardBackground: Color = surface,
    val cardBorder: Color = border,
    val cardBorderHighlight: Color = borderHighlight
)
