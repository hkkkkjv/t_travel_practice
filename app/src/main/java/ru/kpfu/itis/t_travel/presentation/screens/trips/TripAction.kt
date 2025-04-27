package ru.kpfu.itis.t_travel.presentation.screens.trips

sealed class TripAction {
    object Refresh : TripAction()
    data class SelectTrip(val tripId: Int) : TripAction()
}