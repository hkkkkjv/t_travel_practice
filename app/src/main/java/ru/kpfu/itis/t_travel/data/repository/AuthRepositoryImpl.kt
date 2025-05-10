package ru.kpfu.itis.t_travel.data.repository

import ru.kpfu.itis.t_travel.data.model.AuthResponse
import ru.kpfu.itis.t_travel.data.remote.ApiService
import ru.kpfu.itis.t_travel.domain.model.AuthResult
import ru.kpfu.itis.t_travel.domain.model.LoginCredentials
import ru.kpfu.itis.t_travel.domain.model.User
import ru.kpfu.itis.t_travel.domain.repository.AuthRepository
import ru.kpfu.itis.t_travel.presentation.common.TokenManager
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun login(credentials: LoginCredentials): AuthResult {
        return try {
            val response = apiService.login(credentials)
            if (response.isSuccessful) {
                response.body()?.toDomain()?.also { result ->
                    if (result is AuthResult.Success) {
                        tokenManager.saveToken(result.token)
                    }
                } ?: AuthResult.Error("Empty response body")
            } else {
                AuthResult.Error(
                    message = response.errorBody()?.string() ?: "Unknown error",
                    code = response.code()
                )
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Network error")
        }
    }

    override suspend fun register(user: User): AuthResponse {
        TODO("Not yet implemented")
    }
}