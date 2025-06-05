package ru.kpfu.itis.t_travel.presentation.screens.auth.register

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.domain.useCase.auth.RegisterUseCase
import ru.kpfu.itis.t_travel.presentation.common.BaseViewModel
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import ru.kpfu.itis.t_travel.utils.Constants
import ru.kpfu.itis.t_travel.utils.runSuspendCatching
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    @ApplicationContext private val context: Context
) : BaseViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.UsernameChanged -> _state.update { it.copy(username = event.value) }
            is RegisterEvent.EmailChanged -> _state.update { it.copy(email = event.value) }
            is RegisterEvent.PhoneChanged -> _state.update { it.copy(phone = event.value) }
            is RegisterEvent.FirstNameChanged -> _state.update { it.copy(firstName = event.value) }
            is RegisterEvent.LastNameChanged -> _state.update { it.copy(lastName = event.value) }
            is RegisterEvent.PasswordChanged -> _state.update { it.copy(password = event.value) }
            is RegisterEvent.ConfirmPasswordChanged -> _state.update { it.copy(confirmPassword = event.value) }
            is RegisterEvent.RegisterClicked -> register()
            is RegisterEvent.NavigateBack -> {
                navigate(NavigationAction.NavigateBack)
            }

            is RegisterEvent.ErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }

    private fun register() {
        Log.i("register", "")
        viewModelScope.launch {
            val currentState = _state.value
            val validationError = validateRegistrationInput(currentState)

            if (validationError != null) {
                _state.update { it.copy(error = validationError) }
                return@launch
            }
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                runSuspendCatching {
                    registerUseCase(
                        username = _state.value.username,
                        email = _state.value.email,
                        phone = _state.value.phone,
                        firstName = _state.value.firstName,
                        lastName = _state.value.lastName,
                        password = _state.value.password
                    )
                }
                    .onSuccess { user ->
                        navigate(NavigationAction.NavigateToLogin)
                        _state.update { it.copy(isLoading = false) }
                    }.onFailure { error ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = error.message
                                    ?: context.getString(R.string.registration_error)
                            )
                        }
                    }
            } catch (e: Exception) {
                Log.e("RegisterViewModel", context.getString(R.string.registration_error))
                _state.update { it.copy(error = context.getString(R.string.registration_error)) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun validateRegistrationInput(state: RegisterState): String? {
        return when {
            state.firstName.isBlank() -> context.getString(R.string.name_blank_error)
            !state.firstName.matches(Constants.Validation.NAME_REGEX) -> context.getString(R.string.incorrect_name)
            state.lastName.isBlank() -> context.getString(R.string.lastname_blank_error)
            !state.lastName.matches(Constants.Validation.NAME_REGEX) -> context.getString(R.string.incorrect_lastname)
            state.username.isBlank() -> context.getString(R.string.blank_login_error)
            !state.username.matches(Constants.Validation.USERNAME_REGEX) -> context.getString(R.string.incorrect_login)
            state.email.isBlank() -> context.getString(R.string.email_blank_error)
            !state.email.matches(Constants.Validation.EMAIL_REGEX) -> context.getString(R.string.incorrect_email)
            state.phone.isBlank() -> context.getString(R.string.phone_blank_error)
            !state.phone.matches(Constants.Validation.PHONE_REGEX) -> context.getString(R.string.incorrect_phone_number)
            state.password.isBlank() -> context.getString(R.string.password_blank_error)
            !state.password.matches(Constants.Validation.PASSWORD_REGEX) -> context.getString(R.string.password_requirements)
            state.confirmPassword.isBlank() -> context.getString(R.string.password_again)
            state.password != state.confirmPassword -> context.getString(R.string.passwords_do_not_match)
            else -> null
        }
    }
}
