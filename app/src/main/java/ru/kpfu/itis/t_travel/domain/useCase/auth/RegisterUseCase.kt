package ru.kpfu.itis.t_travel.domain.useCase.auth

import ru.kpfu.itis.t_travel.domain.model.AuthResult
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
    ): AuthResult {
        if (username.isBlank()) throw RegistrationException.EmptyUsernameException("")
        if (email.isBlank()) throw RegistrationException.EmptyEmailException("")
        if (phone.isBlank()) throw RegistrationException.EmptyPhoneException("")
        if (firstName.isBlank()) throw RegistrationException.EmptyFirstNameException("")
        if (lastName.isBlank()) throw RegistrationException.EmptyLastNameException("")
        if (password.isBlank()) throw RegistrationException.EmptyPasswordException("")
        if (password.length < 6) throw RegistrationException.ShortPasswordException("")

        return authRepository.register(
            User(
                username = username,
                email = email,
                phone = phone,
                firstName = firstName,
                lastName = lastName,
                password = password,
                id = 0
            )
        )
    }
} 