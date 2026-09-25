package com.example.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.StudyRadii
import com.example.ui.theme.StudySpacing
import com.example.ui.theme.StudyTheme

@Composable
fun GlowCard(
    modifier: Modifier = Modifier,
    glowEnabled: Boolean = true,
    borderEnabled: Boolean = true,
    elevation: Dp = 8.dp,
    shape: Shape = RoundedCornerShape(StudyRadii.large),
    borderColor: Color? = null,
    glowColor: Color? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    contentPadding: Dp = StudySpacing.md,
    testTag: String = "glow_card",
    content: @Composable BoxScope.() -> Unit
) {
    val extendedColors = StudyTheme.extendedColors
    val config = StudyTheme.animationConfig
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null && enabled && !config.reduceMotion) 0.985f else 1.0f,
        animationSpec = tween(100),
        label = "GlowCardScale"
    )

    val activeGlow = glowColor ?: extendedColors.glowStrong
    val activeBorder = borderColor ?: if (glowEnabled) extendedColors.borderHighlight else extendedColors.border

    val effectiveElevation = if (config.reduceMotion || !glowEnabled) 0.dp else elevation * config.glowIntensity

    Box(
        modifier = modifier
            .testTag(testTag)
            .scale(scale)
            .shadow(
                elevation = effectiveElevation,
                shape = shape,
                spotColor = activeGlow.copy(alpha = 0.35f * config.glowIntensity),
                ambientColor = Color.Black.copy(alpha = 0.5f)
            )
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(
                        extendedColors.surfaceElevated.copy(alpha = 0.95f),
                        extendedColors.surface.copy(alpha = 0.90f)
                    )
                )
            )
            .then(
                if (borderEnabled) {
                    Modifier.border(
                        BorderStroke(1.2.dp, activeBorder),
                        shape
                    )
                } else Modifier
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        enabled = enabled,
                        interactionSource = interactionSource,
                        indication = ripple(color = activeGlow),
                        onClick = onClick
                    )
                } else Modifier
            )
            .padding(contentPadding),
        content = content
    )
}
