package ru.kpfu.itis.t_travel.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.kpfu.itis.t_travel.presentation.screens.auth.login.LoginScreen
import ru.kpfu.itis.t_travel.presentation.screens.auth.register.RegisterScreen
import ru.kpfu.itis.t_travel.presentation.screens.auth.welcome.WelcomeScreen

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object TripCreate : Screen("trip_create")
    object Budget : Screen("budget")
    object ExpenseCategories : Screen("expense_categories")
    object BudgetDistribution : Screen("budget_distribution/{tripId}")
    object TripDetails : Screen("trip_details/{tripId}")
    object ExpensesTab : Screen("expenses_tab/{tripId}")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String = Screen.Welcome.route
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                navigateLoginScreen = {
                    handleNavigationAction(NavigationAction.NavigateToLogin, navController)
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigationAction = { action ->
                    handleNavigationAction(action, navController)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigationAction = { action ->
                    handleNavigationAction(action, navController)
                }
            )
        }
    }
}
private fun handleNavigationAction(action: NavigationAction, navController: NavHostController){
    when (action) {
        is NavigationAction.NavigateToHome -> {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
        is NavigationAction.NavigateToLogin -> {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
        is NavigationAction.NavigateToRegister -> {
            navController.navigate(Screen.Register.route)
        }
        is NavigationAction.NavigateToTripCreate -> {
            navController.navigate(Screen.TripCreate.route)
        }
        is NavigationAction.NavigateToTripDetails -> {
            navController.navigate(Screen.TripDetails.route.replace("{tripId}",action.tripId))
        }
        is NavigationAction.NavigateBack -> {
            navController.popBackStack()
        }
        is NavigationAction.NavigateToProfile -> {
            navController.navigate(Screen.Profile.route)
        }
        is NavigationAction.NavigateToSettings -> {
            navController.navigate(Screen.Settings.route)
        }
        is NavigationAction.NavigateToExpensesTab -> {
            navController.navigate(Screen.ExpensesTab.route.replace("{tripId}", action.tripId))
        }
        is NavigationAction.NavigateToBudgetDistribution -> {
            navController.navigate(Screen.BudgetDistribution.route.replace("{tripId}", action.tripId))
        }
        else -> {}
    }
}

