package ru.kpfu.itis.t_travel.presentation.screens.auth.login

sealed class LoginEvent {
    data class UsernameChanged(val value: String) : LoginEvent()
    data class PasswordChanged(val value: String) : LoginEvent()
    object LoginClicked : LoginEvent()
    object RegisterClicked : LoginEvent()
    object BackClicked : LoginEvent()
    object ErrorDismissed : LoginEvent()
}