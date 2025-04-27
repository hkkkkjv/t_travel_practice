package ru.kpfu.itis.t_travel.presentation.screens.trips

import retrofit2.Response
import ru.kpfu.itis.t_travel.data.model.AuthResponse
import ru.kpfu.itis.t_travel.domain.model.LoginCredentials
import ru.kpfu.itis.t_travel.domain.model.Trip
import ru.kpfu.itis.t_travel.domain.model.User
import ru.kpfu.itis.t_travel.domain.repository.TripRepository

class FakeTripRepository : TripRepository {
    override suspend fun getTrips(): List<Trip> = listOf(Trip.mock())
    override suspend fun register(user: User): Response<AuthResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun login(credentials: LoginCredentials): Response<AuthResponse> {
        TODO("Not yet implemented")
    }
}