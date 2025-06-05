package ru.kpfu.itis.t_travel.presentation.screens.trips.details


sealed class TripDetailsEvent {
    data class Load(val tripId: Int) : TripDetailsEvent()
    object BackClicked : TripDetailsEvent()
    object ParticipantsClicked : TripDetailsEvent()
    object ExpensesClicked : TripDetailsEvent()
    object MyDebtsClicked : TripDetailsEvent()
    object OweMeClicked : TripDetailsEvent()
    object AddExpenseClicked : TripDetailsEvent()
    object ErrorShown : TripDetailsEvent()
}