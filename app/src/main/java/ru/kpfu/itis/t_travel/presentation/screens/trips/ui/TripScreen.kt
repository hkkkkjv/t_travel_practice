package ru.kpfu.itis.t_travel.presentation.screens.trips.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kpfu.itis.t_travel.presentation.screens.trips.TripViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.domain.model.Trip
import ru.kpfu.itis.t_travel.presentation.screens.trips.FakeTripViewModel
import ru.kpfu.itis.t_travel.presentation.screens.trips.TripAction
import ru.kpfu.itis.t_travel.presentation.screens.trips.TripEvent

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
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Поездки") },
            actions = {
                IconButton(onClick = { viewModel.handleAction(TripAction.Refresh) }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Обновить")
                }
            })
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.error != null) {
                Text(
                    text = state.error ?: "Ошибка",
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.trips) { trip ->
                        TripItem(
                            trip = trip,
                            onClick = { viewModel.handleAction(TripAction.SelectTrip(trip.id)) }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun TripItem(trip: Trip, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(text = trip.title, style = MaterialTheme.typography.h6)
        Text(
            text = "${trip.startDate} — ${trip.endDate}",
            style = MaterialTheme.typography.body2,
            color = Color.Gray
        )
        Text(
            text = stringResource(
                R.string.participantsAndBudget,
                trip.participants.size,
                trip.budget
            ),
            style = MaterialTheme.typography.body2,
            color = Color.Gray
        )
    }
}
@Preview
@Composable
fun TripScreenPreview(){
    TripScreen(
        viewModel = FakeTripViewModel(),
        onNavigateToTripDetail = {}
    )
}