package ru.kpfu.itis.t_travel.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.kpfu.itis.t_travel.data.model.AuthResponse
import ru.kpfu.itis.t_travel.data.model.TripDto
import ru.kpfu.itis.t_travel.domain.model.LoginCredentials
import ru.kpfu.itis.t_travel.domain.model.User

interface ApiService {
    @GET("v1/trips")
    suspend fun getTrips(): Response<List<TripDto>>

    @POST("v1/register")
    suspend fun register(@Body user: User): Response<AuthResponse>

    @POST("v1/login")
    suspend fun login(@Body credentials: LoginCredentials): Response<AuthResponse>
}