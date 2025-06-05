package ru.kpfu.itis.t_travel.domain.model

sealed interface AuthResult {
    data class Success(
        val token: String,
        val refreshToken: String,
        val refreshExpiresIn: Long,
        val tokenType: String,
        val expiresIn: Long
    ) : AuthResult

    data class Error(
        val message: String,
        val code: Int? = null
    ) : AuthResult

    object Loading : AuthResult

    companion object {
        fun AuthResult.isSuccess(): Boolean = this is Success
        inline fun AuthResult.onSuccess(block: (Success) -> Unit): AuthResult {
            if (this is Success) block(this)
            return this
        }

        inline fun AuthResult.onFailure(block: (Error) -> Unit): AuthResult {
            if (this is Error) block(this)
            return this
        }
    }
}