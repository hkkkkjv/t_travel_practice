package ru.kpfu.itis.t_travel.domain.model

data class AuthResult(
    val token: String,
    val refreshToken: String,
    val refreshExpiresIn: Long,
    val tokenType: String,
    val expiresIn: Long
)