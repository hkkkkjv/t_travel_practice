package ru.kpfu.itis.domain.exception

import retrofit2.HttpException
import ru.kpfu.itis.t_travel.domain.exception.AppException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExceptionProcessor @Inject constructor() {
    fun processException(throwable: Throwable): AppException {
        return when (throwable) {
            is AppException -> throwable
            is HttpException -> {
                when (throwable.code()) {
                    401 -> AppException.AuthException.NotAuthorized("")
                    403 -> AppException.AuthException.AccessDenied("")
                    404 -> AppException.NetworkException.ResourceNotFound("")
                    500 -> AppException.NetworkException.ServerError("", throwable.code())
                    else -> AppException.NetworkException.ServerError(
                        throwable.message ?: "",
                        throwable.code()
                    )
                }
            }

            is SocketTimeoutException -> AppException.NetworkException.TimeOut("")
            is IOException -> AppException.NetworkException.NoInternetException("")
            else -> AppException.UnknownException("")
        }
    }
}