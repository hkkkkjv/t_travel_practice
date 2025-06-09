package ru.kpfu.itis.t_travel.domain.useCase.profile.settings

import ru.kpfu.itis.t_travel.presentation.common.settings.LanguageManager
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val languageManager: LanguageManager
) {
    operator fun invoke(lang: String) = languageManager.saveLanguage(lang)
}