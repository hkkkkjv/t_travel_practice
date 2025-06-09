package ru.kpfu.itis.t_travel.presentation.common.settings

interface FavoriteTripManager {
    fun saveFavoriteTripId(tripId: Int)
    fun getFavoriteTripId(): Int?
    fun clearFavoriteTrip()
}