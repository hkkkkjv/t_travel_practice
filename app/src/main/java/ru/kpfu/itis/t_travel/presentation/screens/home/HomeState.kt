package ru.kpfu.itis.t_travel.presentation.screens.home

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.domain.model.Trip

data class HomeState(
    val favoriteTripId: Int? = null,
    val favoriteTrip: Trip? = null,
    val totalOperationsAmount: Double = 0.0,
    val participants: ImmutableList<Participant> = persistentListOf(),
    val myExpensesAmount: Double = 0.0,
    val myDebtsAmount: Double = 0.0,
    val owedToMeAmount: Double = 0.0,
    val owedToMeParticipants: ImmutableList<Participant> = persistentListOf(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showSetupSteps: Boolean = true
)