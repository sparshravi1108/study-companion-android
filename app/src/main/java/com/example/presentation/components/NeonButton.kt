package com.example.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.StudyRadii
import com.example.ui.theme.StudyTheme
import com.example.ui.theme.SuccessGreen

enum class ButtonVisualState {
    IDLE,
    PRESSED,
    DISABLED,
    LOADING,
    SUCCESS,
    ERROR
}

@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    isSuccess: Boolean = false,
    isError: Boolean = false,
    height: Dp = 52.dp,
    shape: Shape = RoundedCornerShape(StudyRadii.medium),
    icon: (@Composable () -> Unit)? = null,
    testTag: String = "neon_button"
) {
    val extendedColors = StudyTheme.extendedColors
    val config = StudyTheme.animationConfig
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val buttonState = when {
        !enabled -> ButtonVisualState.DISABLED
        isLoading -> ButtonVisualState.LOADING
        isSuccess -> ButtonVisualState.SUCCESS
        isError -> ButtonVisualState.ERROR
        isPressed -> ButtonVisualState.PRESSED
        else -> ButtonVisualState.IDLE
    }

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled && !config.reduceMotion) 0.97f else 1.0f,
        animationSpec = tween(100),
        label = "ButtonScale"
    )

    val (bgBrush, borderColor, elevation) = when (buttonState) {
        ButtonVisualState.DISABLED -> Triple(
            Brush.linearGradient(listOf(Color(0xFF334155), Color(0xFF1E293B))),
            Color.Transparent,
            0.dp
        )
        ButtonVisualState.LOADING -> Triple(
            extendedColors.accentGradient,
            extendedColors.borderHighlight,
            4.dp
        )
        ButtonVisualState.SUCCESS -> Triple(
            Brush.linearGradient(listOf(SuccessGreen, Color(0xFF059669))),
            SuccessGreen,
            8.dp
        )
        ButtonVisualState.ERROR -> Triple(
            Brush.linearGradient(listOf(ErrorRed, Color(0xFFDC2626))),
            ErrorRed,
            8.dp
        )
        ButtonVisualState.PRESSED -> Triple(
            extendedColors.accentGradient,
            extendedColors.primary,
            12.dp
        )
        ButtonVisualState.IDLE -> Triple(
            extendedColors.accentGradient,
            extendedColors.borderHighlight,
            8.dp
        )
    }

    Box(
        modifier = modifier
            .testTag(testTag)
            .fillMaxWidth()
            .height(height)
            .scale(scale)
            .shadow(
                elevation = if (config.reduceMotion) 0.dp else elevation * config.glowIntensity,
                shape = shape,
                spotColor = borderColor.copy(alpha = 0.6f * config.glowIntensity),
                ambientColor = extendedColors.glowSoft.copy(alpha = 0.3f * config.glowIntensity)
            )
            .clip(shape)
            .background(bgBrush)
            .border(BorderStroke(1.2.dp, borderColor), shape)
            .clickable(
                enabled = enabled && !isLoading,
                interactionSource = interactionSource,
                indication = ripple(color = Color.White),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        when (buttonState) {
            ButtonVisualState.LOADING -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.5.dp
                )
            }
            ButtonVisualState.SUCCESS -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Box(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Completed",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            ButtonVisualState.ERROR -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Error",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Box(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Try Again",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            else -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    if (icon != null) {
                        icon()
                        Box(modifier = Modifier.size(8.dp))
                    }
                    Text(
                        text = text,
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun NeonIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp = 48.dp,
    tint: Color? = null,
    enabled: Boolean = true,
    testTag: String = "neon_icon_button"
) {
    val extendedColors = StudyTheme.extendedColors
    val config = StudyTheme.animationConfig
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled && !config.reduceMotion) 0.92f else 1.0f,
        animationSpec = tween(100),
        label = "IconButtonScale"
    )

    val iconTint = tint ?: extendedColors.glowStrong
    val shape = CircleShape

    Box(
        modifier = modifier
            .testTag(testTag)
            .size(size)
            .scale(scale)
            .shadow(
                elevation = if (config.reduceMotion) 0.dp else 6.dp * config.glowIntensity,
                shape = shape,
                spotColor = iconTint.copy(alpha = 0.5f * config.glowIntensity),
                ambientColor = iconTint.copy(alpha = 0.2f)
            )
            .clip(shape)
            .background(extendedColors.surface)
            .border(BorderStroke(1.2.dp, iconTint.copy(alpha = 0.6f)), shape)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = iconTint),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (enabled) iconTint else extendedColors.textTertiary,
            modifier = Modifier.size(size * 0.5f)
        )
    }
}

@Composable
fun CyberOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 52.dp,
    shape: Shape = RoundedCornerShape(StudyRadii.medium),
    icon: (@Composable () -> Unit)? = null,
    testTag: String = "cyber_outlined_button"
) {
    val extendedColors = StudyTheme.extendedColors
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.98f else 1.0f,
        animationSpec = tween(100),
        label = "OutlinedButtonScale"
    )

    Box(
        modifier = modifier
            .testTag(testTag)
            .fillMaxWidth()
            .height(height)
            .scale(scale)
            .clip(shape)
            .background(extendedColors.surface.copy(alpha = 0.8f))
            .border(
                BorderStroke(1.2.dp, extendedColors.borderHighlight.copy(alpha = 0.5f)),
                shape
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = ripple(color = extendedColors.primary),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            if (icon != null) {
                icon()
                Box(modifier = Modifier.size(10.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}
