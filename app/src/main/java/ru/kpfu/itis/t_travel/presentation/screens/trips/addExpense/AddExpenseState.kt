package ru.kpfu.itis.t_travel.presentation.screens.trips.addExpense

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.presentation.screens.trips.budget.BudgetCategoryUi

data class AddExpenseState(
    val tripId: Int = -1,
    val userId: Int = -1,
    val title: String = "",
    val amount: String = "",
    val categories: ImmutableList<BudgetCategoryUi> = persistentListOf(),
    val selectedCategory: BudgetCategoryUi? = null,
    val participants: ImmutableList<Participant> = persistentListOf(),
    val selectedParticipants: ImmutableList<Participant> = persistentListOf(),
    val isLoading: Boolean = false,
    val error: String? = null
)