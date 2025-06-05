package ru.kpfu.itis.t_travel.presentation.screens.trips.addExpense

import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.presentation.screens.trips.budget.BudgetCategoryUi

sealed class AddExpenseEvent {
    data class Load(val tripId: Int) : AddExpenseEvent()
    data class TitleChanged(val value: String) : AddExpenseEvent()
    data class AmountChanged(val value: String) : AddExpenseEvent()
    data class CategoryChanged(val value: BudgetCategoryUi) : AddExpenseEvent()
    data class ParticipantToggled(val participant: Participant, val isSelected: Boolean) : AddExpenseEvent()
    object SubmitClicked : AddExpenseEvent()
    object BackClicked : AddExpenseEvent()
    object ErrorDismissed : AddExpenseEvent()
}