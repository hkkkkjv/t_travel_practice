package ru.kpfu.itis.t_travel.domain.repository

import ru.kpfu.itis.t_travel.domain.model.Trip

interface TripRepository {
    suspend fun getTrips(): List<Trip>
}