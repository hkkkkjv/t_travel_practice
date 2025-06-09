package ru.kpfu.itis.t_travel.presentation.screens.trips.list

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.domain.model.Trip

@Immutable
data class TripState(
    val pendingTrips: ImmutableList<Trip> = persistentListOf(),
    val confirmedTrips: ImmutableList<Trip> = persistentListOf(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentUserId: Int = 2001,
    val participantsByTripId: ImmutableMap<Int, ImmutableList<Participant>> = persistentMapOf(),
    val expensesByTripId: ImmutableMap<Int, ImmutableList<Expense>> = persistentMapOf(),
    val isLoadingParticipants: ImmutableMap<Int, Boolean> = persistentMapOf(),
    val isLoadingExpenses: ImmutableMap<Int, Boolean> = persistentMapOf(),
)