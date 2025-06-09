package ru.kpfu.itis.t_travel.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.kpfu.itis.t_travel.presentation.common.settings.FavoriteTripManager
import javax.inject.Inject

class FavoriteTripManagerImpl @Inject constructor(
    @ApplicationContext context: Context
) : FavoriteTripManager {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


    override fun saveFavoriteTripId(tripId: Int) {
        sharedPreferences.edit { putInt(FAVORITE_TRIP_ID_KEY, tripId) }
    }

    override fun getFavoriteTripId(): Int? {
        val id = sharedPreferences.getInt(FAVORITE_TRIP_ID_KEY, -1)
        return if (id != -1) id else null
    }

    override fun clearFavoriteTrip() {
        sharedPreferences.edit { remove(FAVORITE_TRIP_ID_KEY) }
    }

    companion object {
        private const val FAVORITE_TRIP_ID_KEY = "favorite_trip_id"
        private const val PREFS_NAME = "favorite_trip_prefs"
    }
}

