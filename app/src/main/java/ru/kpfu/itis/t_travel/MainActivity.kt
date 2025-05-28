package ru.kpfu.itis.t_travel

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.t_travel.presentation.common.TokenManager
import ru.kpfu.itis.t_travel.presentation.navigation.Screen
import ru.kpfu.itis.t_travel.presentation.navigation.bottomNavigation.MainHostScreen
import ru.kpfu.itis.t_travel.presentation.theme.TravelTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var tokenManager: TokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val startDestination = if (tokenManager.getToken() != null) {
                Screen.Home.route
            } else {
                Screen.Welcome.route
            }
            TravelTheme {
                val navController = rememberNavController()
                MainHostScreen(navController = navController, startDestination = startDestination)
            }
        }
    }
}