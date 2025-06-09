package ru.kpfu.itis.t_travel.presentation.screens.trips.details

import ru.kpfu.itis.t_travel.domain.model.SettlementItem


sealed class TripDetailsEvent {
    data object Refresh : TripDetailsEvent()
    data class Load(val tripId: Int) : TripDetailsEvent()
    object BackClicked : TripDetailsEvent()
    object ParticipantsClicked : TripDetailsEvent()
    object ExpensesClicked : TripDetailsEvent()
    object MyDebtsClicked : TripDetailsEvent()
    object OweMeClicked : TripDetailsEvent()
    object AddExpenseClicked : TripDetailsEvent()
    object ErrorShown : TripDetailsEvent()
    data object DismissParticipantsSheet : TripDetailsEvent()
    data object DismissExpensesSheet : TripDetailsEvent()
    data object DismissMyDebtsSheet : TripDetailsEvent()
    data object DismissOwedToMeSheet : TripDetailsEvent()
    data class AddParticipantsClicked(val tripId: Int) : TripDetailsEvent()
    data class RequestDebtConfirmation(val settlement: SettlementItem) : TripDetailsEvent()
    data class ConfirmDebtReturn(val settlement: SettlementItem) : TripDetailsEvent()
}