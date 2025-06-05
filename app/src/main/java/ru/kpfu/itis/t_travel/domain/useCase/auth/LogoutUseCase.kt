package ru.kpfu.itis.t_travel.domain.useCase.auth

import ru.kpfu.itis.t_travel.domain.repository.AuthRepository
import ru.kpfu.itis.t_travel.presentation.common.ProfileManager
import ru.kpfu.itis.t_travel.presentation.common.TokenManager
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileManager: ProfileManager,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke() {
        profileManager.clearAvatarUri()
        tokenManager.clearTokens()
        authRepository.logout()
    }
}