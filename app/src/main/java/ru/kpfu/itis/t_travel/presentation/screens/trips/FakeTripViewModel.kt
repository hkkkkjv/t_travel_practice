package ru.kpfu.itis.t_travel.presentation.screens.trips

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.kpfu.itis.t_travel.domain.model.Trip

class FakeTripViewModel : TripViewModel(FakeTripRepository()) {
    override val state = MutableStateFlow(
        TripState(
            trips = listOf(
                Trip.mock()
            ),
            isLoading = false,
            error = null
        )
    )
    override val events = MutableSharedFlow<TripEvent>()

}