package ru.kpfu.itis.t_travel.domain.useCase.auth

import ru.kpfu.itis.t_travel.domain.model.User
import ru.kpfu.itis.t_travel.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        phone: String,
        firstName: String,
        lastName: String,
        password: String
    ): User {
        return authRepository.register(
            username,
            firstName,
            lastName,
            email,
            phone,
            password
        )
    }
} 