package ru.kpfu.itis.t_travel.domain.useCase.profile.settings

import ru.kpfu.itis.t_travel.presentation.common.settings.ThemeManager
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val themeManager: ThemeManager
) {
    operator fun invoke(): String = themeManager.getTheme()
}