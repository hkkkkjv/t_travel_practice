package ru.kpfu.itis.t_travel.data.repository

import android.util.Log
import ru.kpfu.itis.t_travel.data.model.UserRegistrationRequest
import ru.kpfu.itis.t_travel.data.remote.ApiService
import ru.kpfu.itis.t_travel.domain.model.AuthResult
import ru.kpfu.itis.t_travel.domain.model.LoginCredentials
import ru.kpfu.itis.t_travel.domain.model.User
import ru.kpfu.itis.t_travel.domain.repository.AuthRepository
import ru.kpfu.itis.t_travel.presentation.common.settings.TokenManager
import ru.kpfu.itis.t_travel.utils.runSuspendCatching
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager,
) : AuthRepository {
    override suspend fun login(credentials: LoginCredentials): AuthResult {
        val response = apiService.login(credentials)
        runSuspendCatching { apiService.login(credentials) }.onSuccess { response ->
            tokenManager.saveTokens(response.accessToken, response.refreshToken)
        }
        return response.toDomain()
    }

    override suspend fun register(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        password: String
    ): User {
        val request = UserRegistrationRequest(
            username = username,
            firstName = firstName,
            lastName = lastName,
            email = email,
            phone = phone,
            password = password
        )
        return apiService.register(request).toDomain()
    }

    override suspend fun logout() {
        val refreshToken = tokenManager.getRefreshToken()
        Log.i("refreshToken", refreshToken.toString())
        if (refreshToken != null) {
            apiService.logout(mapOf("refreshToken" to refreshToken))
        }

    }

    override suspend fun userId(): Int {
        return apiService.getUserId()
    }
}