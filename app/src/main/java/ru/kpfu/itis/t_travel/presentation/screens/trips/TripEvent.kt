package ru.kpfu.itis.t_travel.presentation.screens.trips

sealed class TripEvent {
    data class ShowError(val message: String) : TripEvent()
    data class NavigateToTripDetail(val tripId: Int) : TripEvent()
}