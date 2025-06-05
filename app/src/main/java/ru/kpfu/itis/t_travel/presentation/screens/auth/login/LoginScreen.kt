package ru.kpfu.itis.t_travel.presentation.screens.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.presentation.common.ui.CustomTextField
import ru.kpfu.itis.t_travel.presentation.common.ui.ErrorDialog
import ru.kpfu.itis.t_travel.presentation.common.ui.PrimaryButton
import ru.kpfu.itis.t_travel.presentation.common.ui.TransparentTopAppBarWithBack

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    ErrorDialog(
        error = state.error,
        onDismiss = { viewModel.onEvent(LoginEvent.ErrorDismissed) }
    )
    InternalLoginScreen(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun InternalLoginScreen(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TransparentTopAppBarWithBack(
                title = context.getString(R.string.login),
                onBackClick = { onEvent(LoginEvent.BackClicked) },
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            CustomTextField(
                value = state.username,
                onValueChange = { onEvent(LoginEvent.UsernameChanged(it)) },
                label = context.getString(R.string.username),
            )
            Spacer(Modifier.height(16.dp))
            CustomTextField(
                value = state.password,
                onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
                label = context.getString(R.string.password),
                isPassword = true
            )
            Row(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 12.dp),
                    text = context.getString(R.string.not_registered_yet),
                    fontSize = 16.sp
                )
                Spacer(Modifier.width(12.dp))
                TextButton(
                    onClick = { onEvent(LoginEvent.RegisterClicked) }
                ) {
                    Text(
                        context.getString(R.string.registration),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            PrimaryButton(
                text = context.getString(R.string.login),
                onClick = { onEvent(LoginEvent.LoginClicked) }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    MaterialTheme {
        InternalLoginScreen(
            state = LoginState(
                username = "",
                password = "",
                isLoading = false,
                error = null,
                isLoggedIn = false
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenLoadingPreview() {
    MaterialTheme {
        InternalLoginScreen(
            state = LoginState(
                username = "ivanivanov",
                password = "password123",
                isLoading = true,
                error = null,
                isLoggedIn = false
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenErrorPreview() {
    MaterialTheme {
        InternalLoginScreen(
            state = LoginState(
                username = "ivanivanov",
                password = "password123",
                isLoading = false,
                error = "Неверный телефон или пароль",
                isLoggedIn = false
            ),
            onEvent = {},
        )
    }
}