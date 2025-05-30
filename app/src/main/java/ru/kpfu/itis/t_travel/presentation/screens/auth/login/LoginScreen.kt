package ru.kpfu.itis.t_travel.presentation.screens.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.presentation.common.ui.AuthTextField
import ru.kpfu.itis.t_travel.presentation.common.ui.PrimaryButton
import ru.kpfu.itis.t_travel.presentation.common.ui.TransparentTopAppBar
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigationAction: (NavigationAction) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = Color.White,
        darkIcons = true
    )
    LaunchedEffect(Unit) {
        viewModel.navigationAction.collect { action ->
            onNavigationAction(action)
        }
    }

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
    Scaffold(
        topBar = {
            TransparentTopAppBar(
                title = stringResource(R.string.login),
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
            AuthTextField(
                value = state.phone,
                onValueChange = { onEvent(LoginEvent.PhoneChanged(it)) },
                label = stringResource(R.string.phone),
                keyboardType = KeyboardType.Phone
            )
            Spacer(Modifier.height(16.dp))
            AuthTextField(
                value = state.password,
                onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
                label = stringResource(R.string.password),
                isPassword = true
            )
            Row(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 12.dp),
                    text = stringResource(R.string.not_registered_yet),
                    fontSize = 16.sp
                )
                Spacer(Modifier.width(12.dp))
                TextButton(
                    onClick = { onEvent(LoginEvent.RegisterClicked) }
                ) {
                    Text(
                        stringResource(R.string.registration),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            PrimaryButton(
                text = stringResource(R.string.login),
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
                phone = "",
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
                phone = "+7 (999) 123-45-67",
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
                phone = "+7 (999) 123-45-67",
                password = "password123",
                isLoading = false,
                error = "Неверный телефон или пароль",
                isLoggedIn = false
            ),
            onEvent = {},
        )
    }
}