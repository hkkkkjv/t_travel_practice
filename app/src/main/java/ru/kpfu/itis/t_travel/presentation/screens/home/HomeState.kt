package ru.kpfu.itis.t_travel.presentation.screens.home

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kpfu.itis.t_travel.domain.model.Budget
import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.domain.model.SettlementItem
import ru.kpfu.itis.t_travel.domain.model.Trip

@Immutable
data class HomeState(
    val favoriteTripId: Int? = null,
    val favoriteTrip: Trip? = null,
    val totalOperationsAmount: Double = 0.0,
    val myExpensesAmount: Double = 0.0,
    val participants: ImmutableList<Participant> = persistentListOf(),
    val debtsParticipants: ImmutableList<Participant> = persistentListOf(),
    val participantsOwedToMe: ImmutableList<Participant> = persistentListOf(),
    val budget: Budget? = null,
    val myExpenses: ImmutableList<Expense> = persistentListOf(),
    val expenses: ImmutableList<Expense> = persistentListOf(),
    val myDebts: ImmutableList<SettlementItem> = persistentListOf(),
    val oweMe: ImmutableList<SettlementItem> = persistentListOf(),
    val totalPayableAmount: Double = 0.0,
    val totalReceivableAmount: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showSetupSteps: Boolean = true,
    val showParticipantsSheet: Boolean = false,
    val showExpensesSheet: Boolean = false,
    val showMyExpensesSheet: Boolean = false,
    val showMyDebtsSheet: Boolean = false,
    val showOweMeSheet: Boolean = false,
)