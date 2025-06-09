package ru.kpfu.itis.t_travel.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kpfu.itis.t_travel.data.local.database.entity.SettlementEntity

@Dao
interface SettlementDao {
    @Query("SELECT * FROM settlements WHERE trip_id = :tripId")
    suspend fun getSettlementsForTrip(tripId: Int): List<SettlementEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettlements(settlements: List<SettlementEntity>)

    @Delete
    suspend fun deleteSettlements(settlements: List<SettlementEntity>)

    @Query("DELETE FROM settlements WHERE trip_id = :tripId")
    suspend fun deleteSettlementsForTrip(tripId: Int)

    @Query("SELECT * FROM settlements WHERE trip_id = :tripId AND last_updated < :timestamp")
    suspend fun getStaleSettlements(tripId: Int, timestamp: Long): List<SettlementEntity>
} 