package ru.kpfu.itis.t_travel.presentation.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.t_travel.domain.model.LoginCredentials
import ru.kpfu.itis.t_travel.domain.useCase.auth.LoginUseCase
import ru.kpfu.itis.t_travel.presentation.common.BaseViewModel
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _loginState.asStateFlow()


    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.PhoneChanged -> {
                _loginState.update { it.copy(phone = event.value) }
            }
            is LoginEvent.PasswordChanged -> {
                _loginState.update { it.copy(password = event.value) }
            }
            is LoginEvent.LoginClicked -> {
                login(LoginCredentials(_loginState.value.phone, _loginState.value.password))
            }
            is LoginEvent.RegisterClicked -> {
                viewModelScope.launch {
                    navigate(NavigationAction.NavigateToRegister)
                }
            }
            is LoginEvent.BackClicked -> {
                viewModelScope.launch {
                    navigate(NavigationAction.NavigateBack)
                }
            }
        }
    }
    private fun login(credentials: LoginCredentials) {
        viewModelScope.launch {
            _loginState.update { it.copy(isLoading = true, error = null) }
            try {
                loginUseCase(_loginState.value.phone, _loginState.value.password)
                navigate(NavigationAction.NavigateToHome)
            } catch (e: Exception) {
                _loginState.update { it.copy(error = e.message ?: "Ошибка входа") }
            } finally {
                _loginState.update { it.copy(isLoading = false) }
            }
        }
    }
}