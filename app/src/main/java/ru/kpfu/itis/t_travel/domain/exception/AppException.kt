package ru.kpfu.itis.t_travel.domain.exception

sealed class AppException : Exception() {
    sealed class NetworkException : AppException() {
        data class NoInternetException(
            override val message: String
        ) : NetworkException()

        data class TimeOut(
            override val message: String
        ) : NetworkException()

        data class ServerError(
            override val message: String,
            val code: Int
        ) : NetworkException()

        data class ResourceNotFound(
            override val message: String
        ) : NetworkException()

        data class Unknown(
            override val message: String
        ) : NetworkException()
    }

    sealed class AuthException : AppException() {
        data class NotAuthorized(
            override val message: String
        ) : AuthException()

        data class AccessDenied(
            override val message: String
        ) : AuthException()
    }

    sealed class UserInputException : AppException() {
        data class EmptyQuery(
            override val message: String
        ) : UserInputException()

        data class LongQuery(
            override val message: String,
            val maxLength: Int
        ) : UserInputException()

        data class InvalidChars(
            override val message: String
        ) : UserInputException()
    }

    sealed class RegistrationException : AppException() {
        data class EmptyUsernameException(
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

    data class UnknownException(
        override val message: String
    ) : AppException()
}