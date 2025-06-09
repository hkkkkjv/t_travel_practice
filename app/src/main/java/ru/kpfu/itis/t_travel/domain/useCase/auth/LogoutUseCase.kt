package ru.kpfu.itis.t_travel.domain.useCase.auth

import ru.kpfu.itis.t_travel.domain.repository.AuthRepository
import ru.kpfu.itis.t_travel.presentation.common.settings.FavoriteTripManager
import ru.kpfu.itis.t_travel.presentation.common.settings.ProfileManager
import ru.kpfu.itis.t_travel.presentation.common.settings.TokenManager
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileManager: ProfileManager,
    private val favoriteTripManager: FavoriteTripManager,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke() {
        authRepository.logout()
        profileManager.clearAvatarUri()
        favoriteTripManager.clearFavoriteTrip()
        tokenManager.clearTokens()
    }
}