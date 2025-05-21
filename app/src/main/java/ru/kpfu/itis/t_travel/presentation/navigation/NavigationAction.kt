package ru.kpfu.itis.t_travel.presentation.navigation

sealed class NavigationAction {
    object NavigateToHome : NavigationAction()
    object NavigateToLogin : NavigationAction()
    object NavigateToRegister : NavigationAction()
    object NavigateToTripCreate : NavigationAction()
    object NavigateToProfile : NavigationAction()
    object NavigateToSettings : NavigationAction()
    object NavigateToBudget : NavigationAction()
    object NavigateToExpenseCategories : NavigationAction()
    data class NavigateToBudgetDistribution(val tripId: String) : NavigationAction()
    data class NavigateToTripDetails(val tripId: String) : NavigationAction()
    data class NavigateToExpensesTab(val tripId: String) : NavigationAction()
    object NavigateBack : NavigationAction()

}