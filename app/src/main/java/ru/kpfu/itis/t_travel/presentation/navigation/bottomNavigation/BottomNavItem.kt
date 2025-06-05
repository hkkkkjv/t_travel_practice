package ru.kpfu.itis.t_travel.presentation.navigation.bottomNavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import ru.kpfu.itis.t_travel.R

sealed class BottomNavItem(val route: String, val icon: ImageVector, val titleRes: Int) {
    object Home : BottomNavItem("home", Icons.Default.Star, R.string.home)
    object Trips : BottomNavItem("trips", Icons.Default.List, R.string.trips)
    object More : BottomNavItem("more", Icons.Default.AccountCircle, R.string.more)
}