package ru.kpfu.itis.t_travel.presentation.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomAvatar(
    name: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    size: Int = 48
) {
    val avatarModifier = modifier
        .size(size.dp)
        .clip(CircleShape)
        .background(backgroundColor)

    val firstLetter = name.firstOrNull()?.uppercaseChar() ?: '?'

    Box(
        modifier = avatarModifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = firstLetter.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = textColor
        )
    }
}
