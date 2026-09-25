package com.example.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.model.AiCompanionState
import com.example.presentation.components.AiOrb
import com.example.presentation.components.EnergyProgressBar
import com.example.presentation.components.InkBackground
import com.example.ui.animation.ParticlePreset
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.StudySpacing
import com.example.ui.theme.StudyTheme
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(0f) }
    val extendedColors = StudyTheme.extendedColors
    val config = StudyTheme.animationConfig

    LaunchedEffect(Unit) {
        val duration = config.duration(1600)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = duration, easing = LinearEasing)
        )
        delay(150)
        onSplashComplete()
    }

    InkBackground(
        modifier = modifier.testTag("splash_screen"),
        particlePreset = ParticlePreset.ENERGY
    ) {
        // Cosmic Artwork with dark vignette overlay
        Image(
            painter = painterResource(id = R.drawable.splash_hero),
            contentDescription = "Cosmic Sky Artwork",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.45f
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.5f),
                            Color.Black.copy(alpha = 0.2f),
                            Color.Black.copy(alpha = 0.85f),
                            extendedColors.background.copy(alpha = 0.95f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = StudySpacing.lg, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 28.dp)
            ) {
                AiOrb(
                    state = AiCompanionState.FOCUS,
                    size = 110.dp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "STUDY",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 4.sp,
                        fontSize = 32.sp
                    )
                )
                Text(
                    text = "COMPANION",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = extendedColors.primary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 6.sp,
                        fontSize = 24.sp
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Your AI-Powered Study Partner",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = extendedColors.textSecondary,
                        letterSpacing = 1.sp
                    )
                )
            }

            // Bottom loading progress
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Loading your journey...",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = extendedColors.textSecondary,
                        fontSize = 12.sp,
                        letterSpacing = 0.5.sp
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                EnergyProgressBar(
                    progress = progress.value,
                    height = 6.dp,
                    showGlowHead = true
                )
            }
        }
    }
}
