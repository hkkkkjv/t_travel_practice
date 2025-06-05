package ru.kpfu.itis.t_travel.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.kpfu.itis.t_travel.R

enum class AppTheme(val nameRes: Int) {
    LIGHT(R.string.theme_light),
    DARK(R.string.theme_dark),
    SYSTEM(R.string.theme_system);

    @Composable
    fun displayName(): String = stringResource(id = nameRes)
}