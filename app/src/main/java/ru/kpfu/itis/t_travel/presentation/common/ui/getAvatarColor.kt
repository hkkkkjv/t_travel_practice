package ru.kpfu.itis.t_travel.presentation.common.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@Composable
fun getAvatarColor(idx: Int): Color = when (idx % 15) {
    0 -> MaterialTheme.colorScheme.primary.copy(0.7f)
    1 -> MaterialTheme.colorScheme.secondary.copy(0.7f)
    3 -> MaterialTheme.colorScheme.primaryContainer.copy(0.7f)
    4 -> MaterialTheme.colorScheme.secondaryContainer.copy(0.7f)
    5 -> MaterialTheme.colorScheme.tertiaryContainer.copy(0.7f)
    6 -> MaterialTheme.colorScheme.error.copy(0.7f)
    7 -> MaterialTheme.colorScheme.errorContainer.copy(0.7f)
    8 -> Color(0xFFFACE8C).copy(0.7f)
    9 -> MaterialTheme.colorScheme.outline.copy(0.7f)
    10 -> Color(0xFFB39DDB) // сиреневый
    11 -> Color(0xFF80CBC4) // бирюзовый
    12 -> Color(0xFFFFAB91) // оранжевый
    13 -> Color(0xFFA5D6A7) // зелёный
    else -> Color(0xFF90CAF9) // голубой
}