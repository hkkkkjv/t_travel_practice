package ru.kpfu.itis.t_travel.presentation.screens.trips

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kpfu.itis.t_travel.domain.model.Trip
@Immutable
data class TripState(
    val trips: ImmutableList<Trip> = persistentListOf(),
    val isLoading: Boolean = false,
    val error: String? = null
)