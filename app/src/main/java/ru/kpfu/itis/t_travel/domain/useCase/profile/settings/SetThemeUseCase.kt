package ru.kpfu.itis.t_travel.domain.useCase.profile.settings

import ru.kpfu.itis.t_travel.presentation.common.settings.ThemeManager
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val themeManager: ThemeManager
) {
    operator fun invoke(theme: String) = themeManager.saveTheme(theme)
}