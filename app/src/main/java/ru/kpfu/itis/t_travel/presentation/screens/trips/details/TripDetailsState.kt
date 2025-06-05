package ru.kpfu.itis.t_travel.presentation.screens.trips.details

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.domain.model.Trip
import ru.kpfu.itis.t_travel.presentation.screens.trips.budget.BudgetCategoryUi

data class TripDetailsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val trip: Trip? = null,
    val totalBudget: Double = 0.0,
    val categories: ImmutableList<BudgetCategoryUi> = persistentListOf(),
    val expenses: ImmutableList<Expense> = persistentListOf(),
    val participants: ImmutableList<Participant> = persistentListOf(),
    val myDebts: ImmutableList<Expense> = persistentListOf(),
    val oweMe: ImmutableList<Expense> = persistentListOf(),
    val showParticipantsSheet: Boolean = false,
    val showExpensesSheet: Boolean = false,
    val showMyDebtsSheet: Boolean = false,
    val showOweMeSheet: Boolean = false,
)