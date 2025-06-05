package ru.kpfu.itis.t_travel.presentation.screens.trips.create

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.presentation.common.ui.CustomTextField
import ru.kpfu.itis.t_travel.presentation.common.ui.PrimaryButton
import ru.kpfu.itis.t_travel.presentation.common.ui.TransparentTopAppBarWithBack
import java.time.LocalDate

@Composable
fun NewTripScreen(
    viewModel: NewTripViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    InternalNewTripScreen(
        state = state,
        onEvent = viewModel::onEvent,
    )

}

@Composable
fun InternalNewTripScreen(
    state: NewTripState,
    onEvent: (NewTripEvent) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TransparentTopAppBarWithBack(
                title = stringResource(R.string.new_trip_title),
                onBackClick = { onEvent(NewTripEvent.BackClicked) },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            CustomTextField(
                value = state.title,
                onValueChange = { onEvent(NewTripEvent.TitleChanged(it)) },
                label = stringResource(R.string.new_trip_name),
            )
            Spacer(Modifier.height(16.dp))
            CustomTextField(
                value = state.description,
                onValueChange = { onEvent(NewTripEvent.DescriptionChanged(it)) },
                label = stringResource(R.string.description),
            )
            Spacer(Modifier.height(16.dp))
            CustomTextField(
                value = state.departureCity,
                onValueChange = { onEvent(NewTripEvent.DepartureCityChanged(it)) },
                label = stringResource(R.string.departure_city),
            )
            Spacer(Modifier.height(16.dp))
            CustomTextField(
                value = state.destinationCity,
                onValueChange = { onEvent(NewTripEvent.DestinationCityChanged(it)) },
                label = stringResource(R.string.destination_city),
            )
            Spacer(Modifier.height(16.dp))
            DateRangeSelector(
                startDate = state.startDate,
                endDate = state.endDate,
                onStartClick = { onEvent(NewTripEvent.ShowStartDatePicker) },
                onEndClick = { onEvent(NewTripEvent.ShowEndDatePicker) },
            )
            Spacer(Modifier.weight(1f))
            PrimaryButton(
                text = stringResource(R.string.next),
                onClick = { onEvent(NewTripEvent.CreateTripClicked) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )
            if (state.isLoading) {
                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }
            state.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
    if (state.showDatePickerStart) {
        val now = LocalDate.now()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                onEvent(NewTripEvent.StartDateSelected(LocalDate.of(year, month + 1, dayOfMonth)))
            },
            state.startDate?.year ?: now.year,
            (state.startDate?.monthValue ?: now.monthValue) - 1,
            state.startDate?.dayOfMonth ?: now.dayOfMonth
        ).show()
    }
    if (state.showDatePickerEnd) {
        val now = LocalDate.now()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                onEvent(NewTripEvent.EndDateSelected(LocalDate.of(year, month + 1, dayOfMonth)))
            },
            state.endDate?.year ?: (state.startDate?.year ?: now.year),
            (state.endDate?.monthValue ?: (state.startDate?.monthValue ?: now.monthValue)) - 1,
            state.endDate?.dayOfMonth ?: (state.startDate?.dayOfMonth ?: now.dayOfMonth)
        ).show()
    }
}

@Composable
fun DateRangeSelector(
    startDate: LocalDate?,
    endDate: LocalDate?,
    onStartClick: () -> Unit,
    onEndClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Surface(
            onClick = onStartClick,
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = startDate?.toString() ?: stringResource(R.string.start),
                    color = if (startDate == null) Color.Gray else Color.Black
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Surface(
            onClick = onEndClick,
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Text(
                    text = endDate?.toString() ?: stringResource(R.string.end),
                    color = if (endDate == null) Color.Gray else Color.Black
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

