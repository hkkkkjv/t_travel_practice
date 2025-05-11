package ru.kpfu.itis.t_travel.presentation.screens.trips.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kpfu.itis.t_travel.presentation.screens.trips.TripViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.persistentListOf
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.model.Trip
import ru.kpfu.itis.t_travel.domain.model.User
import ru.kpfu.itis.t_travel.presentation.screens.trips.TripAction
import ru.kpfu.itis.t_travel.presentation.screens.trips.TripEvent
import ru.kpfu.itis.t_travel.presentation.screens.trips.TripState
import java.time.LocalDate

@Composable
fun TripScreen(
    viewModel: TripViewModel = hiltViewModel(),
    onNavigateToTripDetail: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is TripEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }

                is TripEvent.NavigateToTripDetail -> {
                    onNavigateToTripDetail(event.tripId)
                }
            }
        }
    }
    InternalTripScreen(
        state = state,
        onRefresh = { viewModel.handleAction(TripAction.Refresh) },
        onTripClick = { id -> viewModel.handleAction(TripAction.SelectTrip(id)) }
    )
}

@Composable
fun TripItem(trip: Trip, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(text = trip.title, style = MaterialTheme.typography.titleSmall)
        Text(
            text = "${trip.startDate} — ${trip.endDate}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Text(
            text = stringResource(
                R.string.participants_budget,
                trip.participants.size,
                trip.budget
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
private fun InternalTripScreen(
    state: TripState,
    onRefresh: () -> Unit,
    onTripClick: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.trips)) },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.refresh)
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
                            Button(onClick = onRefresh) {
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
                            items(items = state.trips, key = {it.id}) { trip ->
                                TripItem(
                                    trip = trip,
                                    onClick = { onTripClick(trip.id) }
                                )
                                Divider()
                            }
                        }
                    }
                }
            }

        }
    )
}
@Preview
@Composable
private fun TripScreenPreview() {
    MaterialTheme {
        InternalTripScreen(
            state = TripState(
                trips = persistentListOf(
                    Trip(
                        id = 1,
                        title = "Отпуск в Сочи",
                        startDate = LocalDate.now(),
                        endDate = LocalDate.now().plusDays(7),
                        participants = listOf(User.mock()),
                        budget = 100000.0,
                        expenses = listOf(Expense.mock()),
                        departureCity = "Казань",
                        destinationCity = "Сочи",
                        createdBy = 1
                    )
                )
            ),
            onRefresh = {},
            onTripClick = {}
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