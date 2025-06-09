package ru.kpfu.itis.t_travel.presentation.screens.auth.login

import androidx.compose.runtime.Immutable

@Immutable
data class LoginState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
)