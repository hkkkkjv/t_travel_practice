package ru.kpfu.itis.t_travel.presentation.screens.trips

import ru.kpfu.itis.t_travel.domain.model.Trip

data class TripState(
    val trips: List<Trip> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)