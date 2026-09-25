package com.example.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness5
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.ThemeMode
import com.example.ui.theme.CalmGradient
import com.example.ui.theme.ClassicDarkGradient
import com.example.ui.theme.ExamGradient
import com.example.ui.theme.InkAnimeGradient
import com.example.ui.theme.LightGradient
import com.example.ui.theme.LightningGradient
import com.example.ui.theme.NeonNightGradient
import com.example.ui.theme.StudyRadii
import com.example.ui.theme.StudyTheme

@Composable
fun ThemeSelector(
    selectedTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ThemeMode.values().forEach { mode ->
            ThemeOptionCard(
                themeMode = mode,
                isSelected = selectedTheme == mode,
                onClick = { onThemeSelected(mode) }
            )
        }
    }
}

@Composable
fun ThemeOptionCard(
    themeMode: ThemeMode,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = StudyTheme.extendedColors
    val config = StudyTheme.animationConfig

    val (gradient, icon, swatchColors) = when (themeMode) {
        ThemeMode.LIGHT -> Triple(
            LightGradient,
            Icons.Default.Brightness5,
            listOf(Color(0xFF2563EB), Color(0xFF4F46E5), Color(0xFFF8FAFC))
        )
        ThemeMode.DARK -> Triple(
            ClassicDarkGradient,
            Icons.Default.Nightlight,
            listOf(Color(0xFF38BDF8), Color(0xFF6366F1), Color(0xFF0F172A))
        )
        ThemeMode.INK_ANIME -> Triple(
            InkAnimeGradient,
            Icons.Default.DarkMode,
            listOf(Color(0xFF00F0FF), Color(0xFF8B5CF6), Color(0xFF080C16))
        )
        ThemeMode.NEON_NIGHT -> Triple(
            NeonNightGradient,
            Icons.Default.Palette,
            listOf(Color(0xFFEC4899), Color(0xFF8B5CF6), Color(0xFF090614))
        )
        ThemeMode.CALM -> Triple(
            CalmGradient,
            Icons.Default.Spa,
            listOf(Color(0xFF10B981), Color(0xFF06B6D4), Color(0xFF071112))
        )
        ThemeMode.LIGHTNING -> Triple(
            LightningGradient,
            Icons.Default.FlashOn,
            listOf(Color(0xFFF59E0B), Color(0xFFFBBF24), Color(0xFF0C0A07))
        )
        ThemeMode.EXAM -> Triple(
            ExamGradient,
            Icons.Default.MenuBook,
            listOf(Color(0xFF2563EB), Color(0xFF38BDF8), Color(0xFF080B12))
        )
    }

    val scale by animateFloatAsState(
        targetValue = if (isSelected && !config.reduceMotion) 1.01f else 1.0f,
        animationSpec = tween(150),
        label = "ThemeCardScale"
    )

    val shape = RoundedCornerShape(StudyRadii.medium)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .testTag("theme_option_${themeMode.name.lowercase()}")
            .clip(shape)
            .background(extendedColors.surface)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                brush = if (isSelected) gradient else Brush.linearGradient(listOf(extendedColors.border, extendedColors.border)),
                shape = shape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = extendedColors.primary),
                onClick = onClick
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Theme Icon with subtle background
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(gradient)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(extendedColors.background),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = themeMode.title,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = themeMode.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = extendedColors.textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White.copy(alpha = 0.08f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = themeMode.badge,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = extendedColors.textSecondary,
                                fontSize = 9.sp
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = themeMode.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = extendedColors.textTertiary,
                        fontSize = 11.sp
                    )
                )

                // Mini Color Swatch Pills
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    swatchColors.forEach { c ->
                        Box(
                            modifier = Modifier
                                .size(width = 16.dp, height = 6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(c)
                                .border(0.5.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(3.dp))
                        )
                    }
                }
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(gradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
