package ru.kpfu.itis.t_travel.presentation.navigation.bottomNavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : BottomNavItem("home", Icons.Default.Star, "Главная")
    object Trips : BottomNavItem("trips", Icons.Default.List, "Поездки")
    object More : BottomNavItem("more", Icons.Default.AccountCircle, "Еще")
}