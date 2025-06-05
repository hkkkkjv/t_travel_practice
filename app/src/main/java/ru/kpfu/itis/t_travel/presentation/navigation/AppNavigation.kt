package ru.kpfu.itis.t_travel.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.coroutines.flow.Flow
import ru.kpfu.itis.t_travel.presentation.screens.auth.login.LoginScreen
import ru.kpfu.itis.t_travel.presentation.screens.auth.register.RegisterScreen
import ru.kpfu.itis.t_travel.presentation.screens.auth.welcome.WelcomeScreen
import ru.kpfu.itis.t_travel.presentation.screens.home.HomeScreen
import ru.kpfu.itis.t_travel.presentation.screens.more.MoreScreen
import ru.kpfu.itis.t_travel.presentation.screens.more.profile.ProfileScreen
import ru.kpfu.itis.t_travel.presentation.screens.trips.addExpense.AddExpenseScreen
import ru.kpfu.itis.t_travel.presentation.screens.trips.addParticipants.AddParticipantsScreen
import ru.kpfu.itis.t_travel.presentation.screens.trips.budget.BudgetFlowScreen
import ru.kpfu.itis.t_travel.presentation.screens.trips.create.NewTripScreen
import ru.kpfu.itis.t_travel.presentation.screens.trips.details.TripDetailsScreen
import ru.kpfu.itis.t_travel.presentation.screens.trips.list.TripScreen

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object TripCreate : Screen("trip_create")
    object AddParticipants : Screen("add_participants/{tripId}")
    object AddBudget : Screen("add_budget/{tripId}")
    object AddExpense: Screen("add_expense/{tripId}")
    object Trips : Screen("trips")
    object More : Screen("more")
    object Budget : Screen("budget")
    object BudgetDistribution : Screen("budget_distribution/{tripId}")
    object TripDetails : Screen("trip_details/{tripId}")
    object ExpensesTab : Screen("expenses_tab/{tripId}")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object Participants : Screen("participants/{tripId}")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String = Screen.Welcome.route,
    navigationFlow: Flow<NavigationAction>
) {
    LaunchedEffect(navigationFlow, navController) {
        navigationFlow.collect { action ->
            handleNavigationAction(action, navController)
        }
    }
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                navigateLoginScreen = {
                    handleNavigationAction(NavigationAction.NavigateToLogin, navController)
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen()
        }

        composable(Screen.Register.route) {
            RegisterScreen()
        }
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Trips.route) {
            TripScreen()
        }
        composable(Screen.More.route) {
            MoreScreen()
        }
        composable(Screen.Participants.route) { /* ... */ }
        composable(
            route = Screen.TripDetails.route,
            arguments = listOf(navArgument("tripId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getInt("tripId")
            if (tripId != null && tripId != -1) {
                TripDetailsScreen(
                    tripId = tripId
                )
            }
        }
        composable(
            route = Screen.AddBudget.route,
            arguments = listOf(navArgument("tripId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getInt("tripId")
            if (tripId != null && tripId != -1) {
                BudgetFlowScreen(
                    tripId = tripId
                )
            }
        }

        composable(Screen.Profile.route) { ProfileScreen() }
        composable(Screen.TripCreate.route) {
            NewTripScreen()
        }
        composable(
            route = Screen.AddParticipants.route,
            arguments = listOf(navArgument("tripId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getInt("tripId")
            if (tripId != null && tripId != -1) {
                AddParticipantsScreen(tripId = tripId)
            }
        }
        composable(
            route = Screen.AddExpense.route,
            arguments = listOf(navArgument("tripId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getInt("tripId")
            if (tripId != null && tripId != -1) {
                AddExpenseScreen(tripId = tripId)
            }
        }
    }
}

private fun handleNavigationAction(action: NavigationAction, navController: NavHostController) {
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

        is NavigationAction.NavigateToTrips -> {
            navController.navigate(Screen.Trips.route)
        }

        is NavigationAction.NavigateToMore -> {
            navController.navigate(Screen.More.route)
        }

        is NavigationAction.NavigateToTripCreate -> {
            navController.navigate(Screen.TripCreate.route)
        }

        is NavigationAction.NavigateToAddParticipants -> {
            navController.navigate(Screen.AddParticipants.route.replace("{tripId}", action.tripId))
        }
        is NavigationAction.NavigateToAddExpense -> {
            navController.navigate(Screen.AddExpense.route.replace("{tripId}", action.tripId))
        }

        is NavigationAction.NavigateToAddBudget -> {
            navController.navigate(Screen.AddBudget.route.replace("{tripId}", action.tripId))
        }

        is NavigationAction.NavigateToTripDetails -> {
            navController.navigate(Screen.TripDetails.route.replace("{tripId}", action.tripId))
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
            navController.navigate(
                Screen.BudgetDistribution.route.replace(
                    "{tripId}",
                    action.tripId
                )
            )
        }

        else -> {}
    }
}

