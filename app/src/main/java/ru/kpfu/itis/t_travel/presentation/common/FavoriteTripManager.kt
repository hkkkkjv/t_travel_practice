package ru.kpfu.itis.t_travel.presentation.common

interface FavoriteTripManager {
    fun saveFavoriteTripId(tripId: Int)
    fun getFavoriteTripId(): Int?
    fun clearFavoriteTrip()
}