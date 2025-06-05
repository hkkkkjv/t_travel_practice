package ru.kpfu.itis.t_travel.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.kpfu.itis.t_travel.data.model.AuthResponse


interface TokenRefreshService {
    @POST("v1/refresh")
    suspend fun refreshTokens(@Body body: Map<String, String>): Response<AuthResponse>
}