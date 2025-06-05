package ru.kpfu.itis.t_travel.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.kpfu.itis.t_travel.data.local.database.entity.ParticipantEntity

@Dao
interface ParticipantDao {
    @Query("SELECT * FROM participants WHERE tripId = :tripId")
    fun getParticipantsForTrip(tripId: Int): List<ParticipantEntity>

    @Query("SELECT * FROM participants WHERE id = :participantId")
    suspend fun getParticipantById(participantId: Int): ParticipantEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipant(participant: ParticipantEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipants(participants: List<ParticipantEntity>)

    @Update
    suspend fun updateParticipant(participant: ParticipantEntity)

    @Delete
    suspend fun deleteParticipant(participant: ParticipantEntity)

    @Query("DELETE FROM participants WHERE tripId = :tripId")
    suspend fun deleteParticipantsForTrip(tripId: Int)

    @Query("UPDATE participants SET confirmed = :confirmed WHERE id = :participantId")
    suspend fun updateConfirmationStatus(participantId: Int, confirmed: Boolean)

    @Query("SELECT * FROM participants WHERE tripId = :tripId AND lastUpdated < :timestamp")
    suspend fun getStaleParticipants(tripId: Int, timestamp: Long): List<ParticipantEntity>
} 