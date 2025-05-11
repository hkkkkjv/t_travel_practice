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
    object BudgetDistribution : Screen("budget_distribution")
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
                onLoginClick = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

    }
}


