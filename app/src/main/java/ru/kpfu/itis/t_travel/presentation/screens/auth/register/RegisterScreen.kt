package ru.kpfu.itis.t_travel.presentation.screens.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.presentation.common.ui.AuthTextField
import ru.kpfu.itis.t_travel.presentation.common.ui.PrimaryButton
import ru.kpfu.itis.t_travel.presentation.common.ui.TransparentTopAppBar
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigationAction: (NavigationAction) -> Unit
) {
    val state by viewModel.state.collectAsState()

    InternalRegisterScreen(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun InternalRegisterScreen(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TransparentTopAppBar(
                title = stringResource(R.string.registration),
                onBackClick = { onEvent(RegisterEvent.NavigateBack) },
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            AuthTextField(
                value = state.firstName,
                onValueChange = { onEvent(RegisterEvent.FirstNameChanged(it)) },
                label = stringResource(R.string.name)
            )
            Spacer(Modifier.height(16.dp))
            AuthTextField(
                value = state.lastName,
                onValueChange = { onEvent(RegisterEvent.LastNameChanged(it)) },
                label = stringResource(R.string.lastname)
            )
            Spacer(Modifier.height(16.dp))
            AuthTextField(
                value = state.username,
                onValueChange = { onEvent(RegisterEvent.UsernameChanged(it)) },
                label = stringResource(R.string.username)
            )
            Spacer(Modifier.height(16.dp))
            AuthTextField(
                value = state.phone,
                onValueChange = { onEvent(RegisterEvent.PhoneChanged(it)) },
                label = stringResource(R.string.phone),
                keyboardType = KeyboardType.Phone
            )
            Spacer(Modifier.height(16.dp))
            AuthTextField(
                value = state.password,
                onValueChange = { onEvent(RegisterEvent.PasswordChanged(it)) },
                label = stringResource(R.string.password),
                isPassword = true
            )
            Spacer(Modifier.height(16.dp))
            AuthTextField(
                value = state.confirmPassword,
                onValueChange = { onEvent(RegisterEvent.ConfirmPasswordChanged(it)) },
                label = stringResource(R.string.confirm_password),
                isPassword = true
            )
            Spacer(Modifier.weight(1f))

            PrimaryButton(
                text = stringResource(R.string.registration),
                onClick = { onEvent(RegisterEvent.RegisterClicked) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    MaterialTheme {
        InternalRegisterScreen(
            state = RegisterState(
                firstName = "",
                phone = "",
                password = "",
                isLoading = false,
                error = null,
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenLoadingPreview() {
    MaterialTheme {
        InternalRegisterScreen(
            state = RegisterState(
                firstName = "Иван",
                lastName = "Иванов",
                username = "ivanivanov",
                phone = "+7 (999) 123-45-67",
                password = "password123",
                isLoading = true,
                error = null,
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenErrorPreview() {
    MaterialTheme {
        InternalRegisterScreen(
            state = RegisterState(
                firstName = "Иван",
                lastName = "Иванов",
                username = "ivanivanov",
                phone = "+7 (999) 123-45-67",
                password = "password123",
                isLoading = false,
                error = "Пользователь с таким телефоном уже существует",
            ),
            onEvent = {},
        )
    }
}