package ru.kpfu.itis.t_travel.presentation.screens.auth.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.presentation.common.ui.PrimaryButton

@Composable
fun WelcomeScreen(
    navigateLoginScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(120.dp))

        Image(
            painter = painterResource(R.drawable.t_bank_ru_logo_svg),
            contentDescription = stringResource(R.string.t_bank),
            modifier = Modifier.height(64.dp)
        )

        Spacer(Modifier.height(24.dp))
        Image(
            painter = painterResource(R.drawable.img_welcome),
            contentDescription = stringResource(R.string.welcome),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .aspectRatio(1.2f)
        )
        Spacer(Modifier.weight(1f))
        PrimaryButton(
            text = stringResource(R.string.login),
            onClick = navigateLoginScreen
        )
    }
}