package ru.kpfu.itis.t_travel.domain.model

sealed class AuthResult {
    data class Success(
        val token: String,
        val refreshToken: String? = null,
        val userId: Int
    ) : AuthResult()

    data class Error(
        val message: String,
        val code: Int? = null
    ) : AuthResult()

    object Loading : AuthResult()

    fun isSuccess(): Boolean = this is Success
}