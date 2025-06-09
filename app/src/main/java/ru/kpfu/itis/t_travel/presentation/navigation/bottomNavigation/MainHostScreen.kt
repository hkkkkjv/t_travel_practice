package ru.kpfu.itis.t_travel.presentation.navigation.bottomNavigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.kpfu.itis.t_travel.presentation.navigation.AppNavigation
import ru.kpfu.itis.t_travel.presentation.navigation.AppNavigator
import ru.kpfu.itis.t_travel.presentation.navigation.Screen

@Composable
fun MainHostScreen(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Welcome.route,
    appNavigator: AppNavigator
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Trips.route,
        Screen.More.route,
        Screen.Profile.route
    )
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    navController = navController
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AppNavigation(
                navController = navController,
                startDestination = startDestination,
                navigationFlow = appNavigator.navigationAction
            )
        }
    }
}