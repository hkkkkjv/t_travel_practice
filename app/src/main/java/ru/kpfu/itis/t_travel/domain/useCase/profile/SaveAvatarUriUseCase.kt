package ru.kpfu.itis.t_travel.domain.useCase.profile

import ru.kpfu.itis.t_travel.presentation.common.settings.ProfileManager
import javax.inject.Inject

class SaveAvatarUriUseCase @Inject constructor(
    private val profileManager: ProfileManager
) {
    operator fun invoke(uri: String) = profileManager.saveAvatarUri(uri)
}