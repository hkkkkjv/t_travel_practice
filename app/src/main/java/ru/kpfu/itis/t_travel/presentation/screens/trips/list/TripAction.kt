package ru.kpfu.itis.t_travel.presentation.screens.trips.list

sealed class TripAction {
    object Refresh : TripAction()
    data class SelectTrip(val tripId: Int) : TripAction()
    data class AcceptInvitation(val tripId: Int) : TripAction()
    data class DeclineInvitation(val tripId: Int) : TripAction()
    data object CreateTrip : TripAction()
    data class SetFavorite(val tripId: Int) : TripAction()
}