package ru.kpfu.itis.t_travel.presentation.screens.trips.addParticipants

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.presentation.common.ui.CustomAvatar
import ru.kpfu.itis.t_travel.presentation.common.ui.CustomTextField
import ru.kpfu.itis.t_travel.presentation.common.ui.PrimaryButton
import ru.kpfu.itis.t_travel.presentation.common.ui.TransparentTopAppBar

@Composable
fun AddParticipantsScreen(
    tripId: Int,
    viewModel: AddParticipantsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    InternalAddParticipantsScreen(
        state = state,
        onEvent = viewModel::onEvent,
    )
    LaunchedEffect(tripId) {
        viewModel.init(tripId)
    }
}

@Composable
fun InternalAddParticipantsScreen(
    state: AddParticipantsState,
    onEvent: (AddParticipantsEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TransparentTopAppBar(
                title = stringResource(R.string.add_participants),
                actions = {
                    IconButton(onClick = { onEvent(AddParticipantsEvent.ShowAddSheet) }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = stringResource(R.string.create_trip)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                } else {
                    LazyColumn {
                        items(state.participants) { participant ->
                            ParticipantItem(participant)
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
                PrimaryButton(
                    text = stringResource(R.string.next),
                    onClick = { onEvent(AddParticipantsEvent.NextClicked) },
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            if (state.showAddSheet) {
                AddParticipantBottomSheet(
                    phone = state.phoneInput,
                    onPhoneChange = { onEvent(AddParticipantsEvent.PhoneInputChanged(it)) },
                    onAdd = { onEvent(AddParticipantsEvent.AddClicked) },
                    onDismiss = { onEvent(AddParticipantsEvent.HideAddSheet) },
                    isAdding = state.isAdding
                )
            }

            state.error?.let {
                Snackbar(
                    action = {
                        TextButton(onClick = { onEvent(AddParticipantsEvent.ErrorShown) }) {
                            Text(stringResource(R.string.ok))
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) { Text(it) }
            }
        }
    }
}


@Composable
fun ParticipantItem(participant: Participant) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomAvatar(name = participant.name)
        Spacer(Modifier.width(16.dp))
        Column {
            Text(participant.name, style = MaterialTheme.typography.bodyLarge)
            Text(
                participant.contact,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddParticipantBottomSheet(
    phone: String,
    onPhoneChange: (String) -> Unit,
    onAdd: () -> Unit,
    onDismiss: () -> Unit,
    isAdding: Boolean
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Column(Modifier.padding(16.dp)) {
            Spacer(Modifier.height(8.dp))
            CustomTextField(
                value = phone,
                onValueChange = onPhoneChange,
                label = stringResource(R.string.phone),
                keyboardType = KeyboardType.Phone
            )
            Spacer(Modifier.height(16.dp))
            PrimaryButton(
                text = stringResource(R.string.add_participants),
                onClick = onAdd,
                enabled = !isAdding && phone.isNotBlank(),
            )
        }
    }
}