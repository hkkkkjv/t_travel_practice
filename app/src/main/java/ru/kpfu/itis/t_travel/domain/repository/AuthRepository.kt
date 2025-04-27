package ru.kpfu.itis.t_travel.domain.repository

import ru.kpfu.itis.t_travel.domain.model.AuthResult
import ru.kpfu.itis.t_travel.domain.model.LoginCredentials

interface AuthRepository {
    suspend fun login(credentials: LoginCredentials): AuthResult
}