package ru.kpfu.itis.t_travel.presentation.screens.home

import ru.kpfu.itis.t_travel.domain.model.SettlementItem

sealed class HomeEvent {
    data object Refresh : HomeEvent()
    data object LoadTripDetails : HomeEvent()
    data object AllOperationsClicked : HomeEvent()
    data object ParticipantsListClicked : HomeEvent()
    data object BudgetClicked : HomeEvent()
    data object MyExpensesClicked : HomeEvent()
    data object MyDebtsClicked : HomeEvent()
    data object OwedToMeClicked : HomeEvent()
    data object AddTripClicked : HomeEvent()
    data object SetupProfileClicked : HomeEvent()
    data object CreateTripClicked : HomeEvent()
    data object ReadyToGoClicked : HomeEvent()
    data object DismissParticipantsSheet : HomeEvent()
    data object DismissExpensesSheet : HomeEvent()
    data object DismissMyExpensesSheet : HomeEvent()
    data object ShowOweMeSheet : HomeEvent()
    data object ShowMyDebtsSheet : HomeEvent()
    data object DismissOweMeSheet : HomeEvent()
    data object DismissMyDebtsSheet : HomeEvent()
    data class RequestDebtConfirmation(val settlement: SettlementItem) : HomeEvent()
    data class ConfirmDebtReturn(val settlement:SettlementItem) : HomeEvent()
    data class AddParticipantsClicked(val tripId: Int) : HomeEvent()

}
