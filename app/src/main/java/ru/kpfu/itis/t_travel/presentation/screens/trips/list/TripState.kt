package ru.kpfu.itis.t_travel.presentation.screens.trips.list

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.domain.model.Trip

@Immutable
data class TripState(
    val trips: ImmutableList<Trip> = persistentListOf(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentUserId: Int = 2001,
    val participantsByTripId: PersistentMap<Int, ImmutableList<Participant>> = persistentMapOf(),
    val expensesByTripId: PersistentMap<Int, ImmutableList<Expense>> = persistentMapOf(),
    val isLoadingParticipants: PersistentMap<Int, Boolean> = persistentMapOf(),
    val isLoadingExpenses: PersistentMap<Int, Boolean> = persistentMapOf(),
)