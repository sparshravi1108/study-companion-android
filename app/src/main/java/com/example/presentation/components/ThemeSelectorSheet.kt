package com.example.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.AnimationLevel
import com.example.domain.model.ThemeMode
import com.example.ui.theme.StudySpacing
import com.example.ui.theme.StudyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelectorSheet(
    currentTheme: ThemeMode,
    currentLevel: AnimationLevel,
    currentIntensity: Float,
    currentReduceMotion: Boolean,
    onDismiss: () -> Unit,
    onApplyTheme: (ThemeMode, AnimationLevel, Float, Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedTheme by remember { mutableStateOf(currentTheme) }
    var selectedLevel by remember { mutableStateOf(currentLevel) }
    var animationIntensity by remember { mutableFloatStateOf(currentIntensity) }
    var reduceMotion by remember { mutableStateOf(currentReduceMotion) }

    val extendedColors = StudyTheme.extendedColors

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = extendedColors.surface,
        dragHandle = null,
        modifier = Modifier.testTag("theme_selector_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = StudySpacing.lg, vertical = StudySpacing.md)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Choose Theme",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = extendedColors.textPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Select from 7 signature study styles",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = extendedColors.textSecondary
                        )
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("theme_selector_close")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = extendedColors.textSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 7 Themes Selector
            ThemeSelector(
                selectedTheme = selectedTheme,
                onThemeSelected = { selectedTheme = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Animation & Motion Options Card
            GlowCard(
                modifier = Modifier.fillMaxWidth(),
                glowEnabled = false
            ) {
                Column {
                    Text(
                        text = "Motion & Effects",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = extendedColors.textPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ReduceMotionToggle(
                        reduceMotion = reduceMotion,
                        onToggle = { reduceMotion = it }
                    )

                    if (!reduceMotion) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Animation Intensity",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = extendedColors.textSecondary
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        AnimationIntensitySelector(
                            currentLevel = selectedLevel,
                            onLevelSelected = { selectedLevel = it }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Apply Theme Button
            NeonButton(
                text = "Apply Theme",
                onClick = {
                    onApplyTheme(selectedTheme, selectedLevel, animationIntensity, reduceMotion)
                    onDismiss()
                },
                testTag = "apply_theme_button"
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
