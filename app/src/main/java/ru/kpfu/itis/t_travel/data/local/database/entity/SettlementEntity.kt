package ru.kpfu.itis.t_travel.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "settlements",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["id"],
            childColumns = ["trip_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["trip_id"]),
        Index(value = ["id"])
    ]
)
data class SettlementEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "trip_id")
    val tripId: Int,
    @ColumnInfo(name = "from_participant_id")
    val fromParticipantId: Int,
    @ColumnInfo(name = "to_participant_id")
    val toParticipantId: Int,
    @ColumnInfo(name = "amount")
    val amount: Double,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long = System.currentTimeMillis()
) 