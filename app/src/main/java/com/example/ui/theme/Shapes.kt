package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object StudyRadii {
    val small: Dp = 10.dp
    val medium: Dp = 16.dp
    val large: Dp = 20.dp
    val extraLarge: Dp = 28.dp
    val full: Dp = 999.dp
}

val StudyShapes = Shapes(
    small = RoundedCornerShape(StudyRadii.small),
    medium = RoundedCornerShape(StudyRadii.medium),
    large = RoundedCornerShape(StudyRadii.large),
    extraLarge = RoundedCornerShape(StudyRadii.extraLarge)
)
