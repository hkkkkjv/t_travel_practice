package ru.kpfu.itis.t_travel.domain.repository

import ru.kpfu.itis.t_travel.data.model.AuthResponse
import ru.kpfu.itis.t_travel.domain.model.AuthResult
import ru.kpfu.itis.t_travel.domain.model.LoginCredentials
import ru.kpfu.itis.t_travel.domain.model.User

interface AuthRepository {
    suspend fun login(credentials: LoginCredentials): AuthResult
    suspend fun register(user: User): AuthResult
}