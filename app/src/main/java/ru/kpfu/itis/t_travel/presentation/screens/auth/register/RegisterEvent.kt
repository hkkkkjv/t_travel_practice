package ru.kpfu.itis.t_travel.presentation.screens.auth.register

sealed class RegisterEvent {
    data class UsernameChanged(val value: String) : RegisterEvent()
    data class EmailChanged(val value: String) : RegisterEvent()
    data class PhoneChanged(val value: String) : RegisterEvent()
    data class FirstNameChanged(val value: String) : RegisterEvent()
    data class LastNameChanged(val value: String) : RegisterEvent()
    data class PasswordChanged(val value: String) : RegisterEvent()
    data class ConfirmPasswordChanged(val value: String) : RegisterEvent()
    object RegisterClicked : RegisterEvent()
    object NavigateBack : RegisterEvent()
    object ErrorDismissed : RegisterEvent()
}