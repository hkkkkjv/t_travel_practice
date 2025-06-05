package ru.kpfu.itis.t_travel.presentation.screens.trips.list

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.domain.model.Trip
import ru.kpfu.itis.t_travel.presentation.common.ui.CustomAvatar
import ru.kpfu.itis.t_travel.presentation.common.ui.PrimaryAlertDialog
import ru.kpfu.itis.t_travel.presentation.common.ui.TransparentTopAppBar
import java.time.LocalDate

@Composable
fun TripScreen(
    viewModel: TripViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is TripEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }

                is TripEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    InternalTripScreen(
        state = state,
        onAction = viewModel::handleAction
    )
}

@Composable
fun TripItem(
    trip: Trip,
    participants: ImmutableList<Participant>,
    expenses: ImmutableList<Expense>,
    currentUserId: Int,
    onAction: (TripAction) -> Unit
) {
    val currentUserParticipant = participants.find { it.id == currentUserId }
    Log.i("TEST TAG", "currentUserParticipant $currentUserParticipant")
    val isInvitation = currentUserParticipant != null && !currentUserParticipant.confirmed
    Log.i("TEST TAG", "isInvitation $isInvitation")

    var showFavoriteDialog by remember { mutableStateOf(false) }
    if (showFavoriteDialog) {
        PrimaryAlertDialog(
            title = stringResource(R.string.confirm_action),
            message = { Text(stringResource(R.string.make_trip_favorite, trip.title)) },
            onRetry = {
                onAction(TripAction.SetFavorite(trip.id))
                showFavoriteDialog = false
            },
            onDismiss = { showFavoriteDialog = false }
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                //Потом поменять на !isInvitation оба
                onClick = {
                    if (!isInvitation) {
                        onAction(TripAction.SelectTrip(trip.id))
                    }
                },
                onLongClick = {
                    if (!isInvitation) {
                        showFavoriteDialog = true
                    }
                }
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CustomAvatar(name = trip.title)
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = shortenText(trip.title),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (isInvitation) {
                    val inviter =
                        participants.find { it.id == trip.createdBy }?.name
                            ?: stringResource(R.string.inviter)
                    Text(
                        text = shortenText(
                            stringResource(
                                R.string.inviter_invite_you_to_trip,
                                inviter
                            )
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                } else {
                    val lastExpenseText = if (expenses.isNotEmpty()) {
                        val lastExpense = expenses.last()
                        shortenText(
                            (participants.find { it.id == lastExpense.paidBy }?.name
                                ?: stringResource(R.string.participant)) + ":" + lastExpense.amount + "руб")
                    } else {
                        stringResource(R.string.no_expenses)
                    }
                    Text(
                        text = lastExpenseText,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
        if (isInvitation) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                IconButton(
                    onClick = { onAction(TripAction.DeclineInvitation(trip.id)) },
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.decline_invitation),
                        tint = Color.Unspecified
                    )
                }
                IconButton(
                    onClick = { onAction(TripAction.AcceptInvitation(trip.id)) },
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = stringResource(R.string.accept_invitation),
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}

fun shortenText(text: String, maxLength: Int = 35): String {
    return if (text.length > maxLength) {
        text.take(maxLength - 1) + "…"
    } else {
        text
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun InternalTripScreen(
    state: TripState,
    onAction: (TripAction) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { onAction(TripAction.Refresh) }
    )
    Scaffold(
        topBar = {
            TransparentTopAppBar(
                title = stringResource(R.string.trips),
                actions = {
                    IconButton(onClick = { onAction(TripAction.CreateTrip) }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.create_trip)
                        )
                    }
                }
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    state.error != null -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = state.error,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { onAction(TripAction.Refresh) }) {
                                Text(stringResource(R.string.retry))
                            }
                        }
                    }

                    state.trips.isEmpty() -> {
                        Text(
                            text = stringResource(R.string.no_trips_available),
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    else -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(items = state.trips, key = { it.id }) { trip ->
                                TripItem(
                                    trip = trip,
                                    participants = state.participantsByTripId[trip.id]
                                        ?: persistentListOf(),
                                    expenses = state.expensesByTripId[trip.id]
                                        ?: persistentListOf(),
                                    currentUserId = state.currentUserId,
                                    onAction = onAction
                                )
                            }
                        }
                    }
                }
                PullRefreshIndicator(
                    refreshing = state.isLoading,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Preview
@Composable
fun PreviewTripScreen() {
    MaterialTheme {
        InternalTripScreen(
            state = TripState(
                trips = persistentListOf(
                    Trip(
                        id = 1,
                        title = "Улетаю на Гаити",
                        startDate = LocalDate.now(),
                        endDate = LocalDate.now().plusDays(7),
                        createdBy = 1,
                        departureCity = "Москва",
                        destinationCity = "Гаити",
                        description = "Гаити"
                    ),
                    Trip(
                        id = 2,
                        title = "Это Питер, детка!",
                        startDate = LocalDate.now(),
                        endDate = LocalDate.now().plusDays(3),
                        createdBy = 3,
                        departureCity = "Казань",
                        destinationCity = "Санкт-Петербург",
                        description = "Питер"
                    ),
                    Trip(
                        id = 3,
                        title = "На недельку до второго...",
                        startDate = LocalDate.now(),
                        endDate = LocalDate.now().plusDays(7),
                        createdBy = 4,
                        departureCity = "Дома",
                        destinationCity = "Дача",
                        description = "Дача"
                    ),
                    Trip(
                        id = 4,
                        title = "СИБИИИИИИРЬ",
                        startDate = LocalDate.now(),
                        endDate = LocalDate.now().plusDays(10),
                        createdBy = 5,
                        departureCity = "Москва",
                        destinationCity = "Красноярск",
                        description = "СИБИИИИИИРЬ"
                    )
                ),
                isLoading = false,
                currentUserId = 2
            ),
            onAction = {}
        )
    }
}

@Preview(widthDp = 50, heightDp = 50)
@Composable
fun SquareComposablePreview() {
    Box(Modifier.background(Color.Yellow)) {
        Text("Hello World")
    }
}