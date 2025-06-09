package ru.kpfu.itis.t_travel

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.t_travel.presentation.common.permission.PermissionHandler
import ru.kpfu.itis.t_travel.presentation.common.settings.TokenManager
import ru.kpfu.itis.t_travel.presentation.navigation.AppNavigator
import ru.kpfu.itis.t_travel.presentation.navigation.Screen
import ru.kpfu.itis.t_travel.presentation.navigation.bottomNavigation.MainHostScreen
import ru.kpfu.itis.t_travel.presentation.theme.TravelTheme
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var appNavigator: AppNavigator
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setBackgroundDrawableResource(R.color.colorSplashBackground)
        super.onCreate(savedInstanceState)
        initializePermissionHandler()
        setContent {
            val startDestination = if (tokenManager.getAccessToken() != null) {
                Screen.Home.route
            } else {
                Screen.Welcome.route
            }
            TravelTheme {
                val navController = rememberNavController()
                MainHostScreen(
                    navController = navController,
                    startDestination = startDestination,
                    appNavigator = appNavigator
                )
            }
        }
    }

    private fun initializePermissionHandler() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionHandler = PermissionHandler(
                onSinglePermissionGranted = { onSinglePermissionGranted() },
                onSinglePermissionDenied = { onSinglePermissionDenied() })
            permissionHandler.initContracts(activity = this)
            if (this.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionHandler.requestSinglePermission(android.Manifest.permission.POST_NOTIFICATIONS)
            }

        }
    }

    private fun onSinglePermissionGranted() {

    }

    private fun onSinglePermissionDenied() {
    }
}