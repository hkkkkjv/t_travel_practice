package ru.kpfu.itis.t_travel.presentation.screens.auth.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.domain.exception.AppException
import ru.kpfu.itis.t_travel.domain.useCase.auth.RegisterUseCase
import ru.kpfu.itis.t_travel.domain.useCase.auth.RegistrationException
import ru.kpfu.itis.t_travel.presentation.common.BaseViewModel
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
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
            is RegisterEvent.NavigateBack->{
                viewModelScope.launch {
                    navigate(NavigationAction.NavigateBack)
                }
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                if (_state.value.password != _state.value.confirmPassword) {
                    _state.update { it.copy(error = context.getString(R.string.passwords_do_not_match)) }
                    return@launch
                }
                registerUseCase(
                    username = _state.value.username,
                    email = _state.value.email,
                    phone = _state.value.phone,
                    firstName = _state.value.firstName,
                    lastName = _state.value.lastName,
                    password = _state.value.password
                )
                navigate(NavigationAction.NavigateToLogin)
            } catch (e: Exception) {
                _state.update { it.copy(error = getMessage(e)) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun getMessage(e: Throwable): String = when (e) {
        is RegistrationException.EmptyUsernameException -> context.getString(R.string.blank_login_error)
        is RegistrationException.EmptyEmailException -> context.getString(R.string.email_blank_error)
        is RegistrationException.EmptyPhoneException -> context.getString(R.string.phone_blank_error)
        is RegistrationException.EmptyFirstNameException -> context.getString(R.string.name_blank_error)
        is RegistrationException.EmptyLastNameException -> context.getString(R.string.lastname_blank_error)
        is RegistrationException.EmptyPasswordException -> context.getString(R.string.password_blank_error)
        is RegistrationException.ShortPasswordException -> context.getString(R.string.password_too_short_error)
        else -> context.getString(R.string.registration_error)
    }
}
