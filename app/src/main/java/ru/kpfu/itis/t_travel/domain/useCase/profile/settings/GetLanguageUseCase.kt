package ru.kpfu.itis.t_travel.domain.useCase.profile.settings

import ru.kpfu.itis.t_travel.presentation.common.LanguageManager
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val languageManager: LanguageManager
) {
    operator fun invoke(): String = languageManager.getLanguage()
}