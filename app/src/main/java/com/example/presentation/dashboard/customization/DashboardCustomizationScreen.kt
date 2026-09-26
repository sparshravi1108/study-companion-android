package com.example.presentation.dashboard.customization

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.dashboard.model.DashboardWidgetConfig
import com.example.presentation.components.InkBackground
import com.example.presentation.components.NeonButton
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.StudySpacing
import com.example.ui.theme.StudyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardCustomizationScreen(
    currentConfigs: List<DashboardWidgetConfig>,
    onBackClick: () -> Unit,
    onSaveConfigs: (List<DashboardWidgetConfig>) -> Unit,
    modifier: Modifier = Modifier
) {
    var configs by remember(currentConfigs) {
        mutableStateOf(currentConfigs.sortedBy { it.order })
    }

    val extendedColors = StudyTheme.extendedColors

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Customize Dashboard",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = extendedColors.textPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Choose what you want to see on your home screen",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = extendedColors.textSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = extendedColors.textPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.testTag("dashboard_customization_screen")
    ) { innerPadding ->
        InkBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            showParticles = false
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = StudySpacing.lg, vertical = StudySpacing.sm)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(configs, key = { _, item -> item.widgetId.name }) { index, item ->
                        WidgetCustomizationRow(
                            config = item,
                            canMoveUp = index > 0,
                            canMoveDown = index < configs.size - 1,
                            onToggleVisible = { newVisible ->
                                configs = configs.map {
                                    if (it.widgetId == item.widgetId) it.copy(visible = newVisible) else it
                                }
                            },
                            onMoveUp = {
                                if (index > 0) {
                                    val mutable = configs.toMutableList()
                                    val prev = mutable[index - 1]
                                    mutable[index - 1] = item.copy(order = index - 1)
                                    mutable[index] = prev.copy(order = index)
                                    configs = mutable
                                }
                            },
                            onMoveDown = {
                                if (index < configs.size - 1) {
                                    val mutable = configs.toMutableList()
                                    val next = mutable[index + 1]
                                    mutable[index + 1] = item.copy(order = index + 1)
                                    mutable[index] = next.copy(order = index)
                                    configs = mutable
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                NeonButton(
                    text = "Save Changes",
                    onClick = {
                        onSaveConfigs(configs)
                        onBackClick()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    height = 50.dp,
                    testTag = "save_dashboard_changes_button"
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun WidgetCustomizationRow(
    config: DashboardWidgetConfig,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onToggleVisible: (Boolean) -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    val extendedColors = StudyTheme.extendedColors
    val shape = RoundedCornerShape(14.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(extendedColors.surface)
            .border(1.dp, extendedColors.border, shape)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Drag / Reorder control arrows
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = onMoveUp,
                enabled = canMoveUp,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Move Up",
                    tint = if (canMoveUp) extendedColors.primary else extendedColors.textTertiary.copy(alpha = 0.3f),
                    modifier = Modifier.size(18.dp)
                )
            }
            IconButton(
                onClick = onMoveDown,
                enabled = canMoveDown,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Move Down",
                    tint = if (canMoveDown) extendedColors.primary else extendedColors.textTertiary.copy(alpha = 0.3f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Title
        Text(
            text = config.widgetId.title,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = if (config.visible) extendedColors.textPrimary else extendedColors.textTertiary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            ),
            modifier = Modifier.weight(1f)
        )

        // Switch
        Switch(
            checked = config.visible,
            onCheckedChange = onToggleVisible,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = NeonCyan,
                uncheckedThumbColor = extendedColors.textTertiary,
                uncheckedTrackColor = extendedColors.surfaceHighlight
            ),
            modifier = Modifier.testTag("switch_${config.widgetId.name.lowercase()}")
        )
    }
}
