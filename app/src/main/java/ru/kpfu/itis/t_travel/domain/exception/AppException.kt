package ru.kpfu.itis.t_travel.domain.exception

open class AppException : Exception() {
    open class NetworkException : AppException() {
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

    data class UnknownException(
        override val message: String
    ) : AppException()
}