package ru.kpfu.itis.t_travel.domain.useCase.auth

import ru.kpfu.itis.t_travel.domain.model.AuthResult
import ru.kpfu.itis.t_travel.domain.model.LoginCredentials
import ru.kpfu.itis.t_travel.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phone: String, password: String): AuthResult {
        if (phone.isBlank()) {
            throw IllegalArgumentException()
        }
        if (password.isBlank()) {
            throw IllegalArgumentException()
        }
        return authRepository.login(LoginCredentials(phone, password))
    }
}
