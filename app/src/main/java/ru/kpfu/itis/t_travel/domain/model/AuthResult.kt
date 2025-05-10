package ru.kpfu.itis.t_travel.domain.model

sealed interface AuthResult {
    data class Success(
        val token: String,
        val refreshToken: String? = null,
        val userId: Int
    ) : AuthResult

    data class Error(
        val message: String,
        val code: Int? = null
    ) : AuthResult

    object Loading : AuthResult

    companion object {
        fun AuthResult.isSuccess(): Boolean = this is Success
    }
}