package ru.kpfu.itis.t_travel.presentation.screens.more

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.presentation.common.ui.TransparentTopAppBar

@Composable
fun MoreScreen(
    viewModel: MoreViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = {
            TransparentTopAppBar(title = stringResource(R.string.additional_information))
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            MoreMenuItem(
                stringResource(R.string.profile),
                R.drawable.ic_profile_more,
                onClick = { viewModel.onEvent(MoreEvent.OnProfileClick) })
            MoreMenuItem(
                stringResource(R.string.history),
                R.drawable.ic_history_more,
                onClick = { viewModel.onEvent(MoreEvent.OnHistoryClick) })
            MoreMenuItem(
                stringResource(R.string.notifications),
                R.drawable.ic_notification_more,
                onClick = { viewModel.onEvent(MoreEvent.OnNotificationsClick) })
            MoreMenuItem(
                stringResource(R.string.debts),
                R.drawable.ic_debts_more,
                onClick = { viewModel.onEvent(MoreEvent.OnDebtsClick) })
        }
    }
}

@Composable
fun MoreMenuItem(
    text: String,
    iconResId: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.extraLarge,
        onClick = onClick,
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.background,
            disabledContentColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(iconResId),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified
            )
            Spacer(Modifier.width(16.dp))
            Text(text, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}