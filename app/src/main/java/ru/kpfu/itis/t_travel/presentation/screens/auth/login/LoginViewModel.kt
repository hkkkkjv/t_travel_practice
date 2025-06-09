package ru.kpfu.itis.t_travel.presentation.screens.auth.login

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
import ru.kpfu.itis.t_travel.domain.model.LoginCredentials
import ru.kpfu.itis.t_travel.domain.useCase.auth.LoginUseCase
import ru.kpfu.itis.t_travel.presentation.common.BaseViewModel
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import ru.kpfu.itis.t_travel.utils.Constants
import ru.kpfu.itis.t_travel.utils.runSuspendCatching
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    @ApplicationContext private val context: Context
) : BaseViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _loginState.asStateFlow()

    init {
        Log.i("LANG_TEST", context.getString(R.string.login))
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.UsernameChanged -> {
                _loginState.update { it.copy(username = event.value) }
            }

            is LoginEvent.PasswordChanged -> {
                _loginState.update { it.copy(password = event.value) }
            }

            is LoginEvent.LoginClicked -> {
                login(LoginCredentials(_loginState.value.username, _loginState.value.password))
            }

            is LoginEvent.RegisterClicked -> {
                navigate(NavigationAction.NavigateToRegister)
            }

            is LoginEvent.BackClicked -> {
                navigate(NavigationAction.NavigateBack)
            }

            is LoginEvent.ErrorDismissed -> _loginState.update { it.copy(error = null) }
        }
    }

    private fun login(credentials: LoginCredentials) {
        val validationError = validateLoginInput(_loginState.value)
        if (validationError != null) {
            _loginState.update { it.copy(error = validationError) }
            return
        }
        viewModelScope.launch {
            _loginState.update { it.copy(isLoading = true, error = null) }
            try {
                runSuspendCatching {
                    loginUseCase(_loginState.value.username, _loginState.value.password)
                }.onSuccess { user ->
                    navigate(NavigationAction.NavigateToHome)
                    _loginState.update { it.copy(isLoading = false, isLoggedIn = true) }
                }.onFailure { error ->
                    _loginState.update { it.copy(isLoading = false, error = error.message) }
                }
            } catch (e: Exception) {
                _loginState.update { it.copy(error = e.message ?: context.getString(R.string.login_error)) }
            } finally {
                _loginState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun validateLoginInput(state: LoginState): String? {
        return when {
            state.username.isBlank() -> context.getString(R.string.username_cannot_be_empty)
            !state.username.matches(Constants.Validation.USERNAME_REGEX) -> context.getString(R.string.invalid_username)
            state.password.isBlank() -> context.getString(R.string.password_blank_error)
            else -> null
        }
    }
}