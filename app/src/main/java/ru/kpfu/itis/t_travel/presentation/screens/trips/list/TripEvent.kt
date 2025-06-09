package ru.kpfu.itis.t_travel.presentation.screens.trips.list

sealed class TripEvent {
    data class ShowError(val message: String) : TripEvent()
    data class ShowMessage(val message: String) : TripEvent()
}