package ru.kpfu.itis.t_travel.domain.repository

import kotlinx.collections.immutable.ImmutableList
import ru.kpfu.itis.t_travel.domain.model.Trip

interface TripRepository {
    suspend fun getTrips(): ImmutableList<Trip>
}