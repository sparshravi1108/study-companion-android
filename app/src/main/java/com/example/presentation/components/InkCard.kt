package com.example.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.StudyTheme

@Composable
fun InkCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    hasGlowBorder: Boolean = false,
    onClick: (() -> Unit)? = null,
    testTag: String = "ink_card",
    content: @Composable BoxScope.() -> Unit
) {
    val extendedColors = StudyTheme.extendedColors
    val shape = RoundedCornerShape(cornerRadius)

    val borderStroke = if (hasGlowBorder) {
        BorderStroke(1.2.dp, extendedColors.cardBorderHighlight)
    } else {
        BorderStroke(1.dp, extendedColors.cardBorder)
    }

    Box(
        modifier = modifier
            .testTag(testTag)
            .shadow(
                elevation = if (hasGlowBorder) 8.dp else 2.dp,
                shape = shape,
                spotColor = if (hasGlowBorder) extendedColors.glowColor.copy(alpha = 0.3f) else Color.Transparent,
                ambientColor = Color.Black.copy(alpha = 0.4f)
            )
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(
                        extendedColors.cardBackground.copy(alpha = 0.95f),
                        extendedColors.cardBackground.copy(alpha = 0.85f)
                    )
                )
            )
            .border(borderStroke, shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = extendedColors.glowColor),
                        onClick = onClick
                    )
                } else Modifier
            )
            .padding(16.dp),
        content = content
    )
}
