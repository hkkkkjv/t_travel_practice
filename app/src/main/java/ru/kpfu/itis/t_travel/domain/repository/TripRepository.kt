package ru.kpfu.itis.t_travel.domain.repository

import retrofit2.Response
import ru.kpfu.itis.t_travel.data.model.AuthResponse
import ru.kpfu.itis.t_travel.domain.model.LoginCredentials
import ru.kpfu.itis.t_travel.domain.model.Trip
import ru.kpfu.itis.t_travel.domain.model.User

interface TripRepository {
    suspend fun getTrips(): List<Trip>
    suspend fun register(user: User): Response<AuthResponse>
    suspend fun login(credentials: LoginCredentials): Response<AuthResponse>
}