package ru.kpfu.itis.t_travel.presentation.screens.home

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
}
