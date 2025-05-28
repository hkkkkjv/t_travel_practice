package ru.kpfu.itis.t_travel.presentation.navigation

sealed class NavigationAction {
    object NavigateToHome : NavigationAction()
    object NavigateToTrips: NavigationAction()
    object NavigateToMore: NavigationAction()
    object NavigateToLogin : NavigationAction()
    object NavigateToRegister : NavigationAction()
    object NavigateToTripCreate : NavigationAction()
    object NavigateToProfile : NavigationAction()
    object NavigateToSettings : NavigationAction()
    object NavigateToBudget : NavigationAction()
    object NavigateToExpenseCategories : NavigationAction()
    data class NavigateToBudgetDistribution(val tripId: String) : NavigationAction()
    data class NavigateToTripDetails(val tripId: String) : NavigationAction()
    data class NavigateToExpensesTab(val tripId: String, val filterByCurrentUser: Boolean = false) : NavigationAction()
    object NavigateBack : NavigationAction()
    object CloseBottomSheet : NavigationAction()
    data class NavigateToParticipants(val tripId: String) : NavigationAction()
    data class NavigateToMyDebts(val tripId: String, val filterByOwedToMe: Boolean = false) : NavigationAction() // Пример с фильтром
}