package ru.kpfu.itis.t_travel.domain.repository

import ru.kpfu.itis.t_travel.domain.model.AuthResult
import ru.kpfu.itis.t_travel.domain.model.LoginCredentials
import ru.kpfu.itis.t_travel.domain.model.User

interface AuthRepository {
    suspend fun login(credentials: LoginCredentials): AuthResult
    suspend fun register(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        password: String
    ): User

    suspend fun logout()
    suspend fun userId(): Int
}