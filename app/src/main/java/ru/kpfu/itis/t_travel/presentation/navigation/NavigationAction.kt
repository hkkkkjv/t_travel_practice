package ru.kpfu.itis.t_travel.presentation.navigation

sealed class NavigationAction {
    object NavigateToHome : NavigationAction()
    object NavigateToTrips : NavigationAction()
    object NavigateToMore : NavigationAction()
    object NavigateToLogin : NavigationAction()
    object NavigateToRegister : NavigationAction()
    object NavigateToTripCreate : NavigationAction()
    data class NavigateToAddParticipants(val tripId: String, val fromBottomSheet: Boolean = false) :
        NavigationAction()

    data class NavigateToAddExpense(val tripId: String) : NavigationAction()
    data class NavigateToAddBudget(val tripId: String) : NavigationAction()
    object NavigateToProfile : NavigationAction()
    object NavigateToEditProfile : NavigationAction()
    object NavigateToHistory : NavigationAction()
    object NavigateToNotifications : NavigationAction()
    object NavigateToDebts : NavigationAction()
    object NavigateToSettings : NavigationAction()
    object NavigateToBudget : NavigationAction()
    object NavigateToExpenseCategories : NavigationAction()
    data class NavigateToAllExpenses(val tripId: String) : NavigationAction()
    data class NavigateToBudgetDistribution(val tripId: String) : NavigationAction()
    data class NavigateToTripDetails(val tripId: String) : NavigationAction()
    data class NavigateToExpensesTab(val tripId: String, val filterByCurrentUser: Boolean = false) :
        NavigationAction()

    object NavigateBack : NavigationAction()
    data class NavigateToParticipants(val tripId: String) : NavigationAction()
    data class NavigateToMyDebts(val tripId: String, val filterByOwedToMe: Boolean = false) :
        NavigationAction()
}