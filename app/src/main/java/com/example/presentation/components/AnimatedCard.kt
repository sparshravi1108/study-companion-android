package com.example.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.ui.theme.StudyRadii
import com.example.ui.theme.StudySpacing
import com.example.ui.theme.StudyTheme

@Composable
fun AnimatedCard(
    modifier: Modifier = Modifier,
    delayMs: Int = 0,
    shape: Shape = RoundedCornerShape(StudyRadii.large),
    borderColor: Color? = null,
    glowColor: Color? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    contentPadding: Dp = StudySpacing.md,
    testTag: String = "animated_card",
    content: @Composable BoxScope.() -> Unit
) {
    val extendedColors = StudyTheme.extendedColors
    val config = StudyTheme.animationConfig

    // Entrance animation
    val enterAlpha = remember { Animatable(if (config.reduceMotion) 1f else 0f) }
    val enterOffsetY = remember { Animatable(if (config.reduceMotion) 0f else 24f) }
    val enterScale = remember { Animatable(if (config.reduceMotion) 1f else 0.95f) }

    LaunchedEffect(Unit) {
        if (!config.reduceMotion) {
            kotlinx.coroutines.delay(delayMs.toLong())
            val duration = config.duration(400)
            enterAlpha.animateTo(1f, tween(duration, easing = FastOutSlowInEasing))
        }
    }

    LaunchedEffect(Unit) {
        if (!config.reduceMotion) {
            kotlinx.coroutines.delay(delayMs.toLong())
            val duration = config.duration(450)
            enterOffsetY.animateTo(0f, tween(duration, easing = FastOutSlowInEasing))
        }
    }

    LaunchedEffect(Unit) {
        if (!config.reduceMotion) {
            kotlinx.coroutines.delay(delayMs.toLong())
            val duration = config.duration(400)
            enterScale.animateTo(1f, tween(duration, easing = FastOutSlowInEasing))
        }
    }

    // Interaction response
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val pressScale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null && enabled && !config.reduceMotion) 0.98f else 1.0f,
        animationSpec = tween(100),
        label = "PressScale"
    )

    val activeGlow = glowColor ?: extendedColors.glowStrong
    val activeBorder = if (isPressed) activeGlow else (borderColor ?: extendedColors.border)

    Box(
        modifier = modifier
            .testTag(testTag)
            .offset { IntOffset(0, enterOffsetY.value.dp.roundToPx()) }
            .alpha(enterAlpha.value)
            .scale(enterScale.value * pressScale)
            .shadow(
                elevation = if (config.reduceMotion) 2.dp else (if (isPressed) 12.dp else 6.dp) * config.glowIntensity,
                shape = shape,
                spotColor = activeGlow.copy(alpha = 0.35f * config.glowIntensity),
                ambientColor = Color.Black.copy(alpha = 0.5f)
            )
            .clip(shape)
            .background(extendedColors.surface)
            .border(BorderStroke(1.2.dp, activeBorder), shape)
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
