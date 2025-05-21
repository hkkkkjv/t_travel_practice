package ru.kpfu.itis.t_travel.domain.useCase.auth

import ru.kpfu.itis.t_travel.domain.exception.AppException

open class RegistrationException : AppException() {
    class EmptyUsernameException(
        override val message: String
    ) : RegistrationException()

    data class EmptyEmailException(
        override val message: String
    ) : RegistrationException()

    data class EmptyPhoneException(
        override val message: String
    ) : RegistrationException()

    data class EmptyFirstNameException(
        override val message: String
    ) : RegistrationException()

    data class EmptyLastNameException(
        override val message: String
    ) : RegistrationException()

    data class EmptyPasswordException(
        override val message: String
    ) : RegistrationException()

    data class ShortPasswordException(
        override val message: String
    ) : RegistrationException()
}