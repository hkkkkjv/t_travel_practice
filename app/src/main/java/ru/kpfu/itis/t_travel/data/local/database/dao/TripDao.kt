package ru.kpfu.itis.t_travel.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.kpfu.itis.t_travel.data.local.database.entity.TripEntity

@Dao
interface TripDao {
    @Query("SELECT * FROM trips ORDER BY startDate DESC")
    fun getAllTrips(): List<TripEntity>

    @Query("SELECT * FROM trips WHERE id = :tripId")
    suspend fun getTripById(tripId: Int): TripEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrips(trips: List<TripEntity>)

    @Update
    suspend fun updateTrip(trip: TripEntity)

    @Delete
    suspend fun deleteTrip(trip: TripEntity)

    @Query("DELETE FROM trips")
    suspend fun deleteAllTrips()

    @Query("UPDATE trips SET isFavorite = :isFavorite WHERE id = :tripId")
    suspend fun updateFavoriteStatus(tripId: Int, isFavorite: Boolean)

    @Query("SELECT * FROM trips WHERE lastUpdated < :timestamp")
    suspend fun getStaleTrips(timestamp: Long): List<TripEntity>
} 