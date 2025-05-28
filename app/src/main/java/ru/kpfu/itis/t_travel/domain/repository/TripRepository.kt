package ru.kpfu.itis.t_travel.domain.repository

import ru.kpfu.itis.t_travel.domain.model.Trip

interface TripRepository {
    suspend fun getTrips(): Result<List<Trip>>
    suspend fun getTripDetails(tripId: Int): Result<Trip>
    suspend fun confirmParticipation(tripId: Int, participantId: Int): Result<Unit>
}