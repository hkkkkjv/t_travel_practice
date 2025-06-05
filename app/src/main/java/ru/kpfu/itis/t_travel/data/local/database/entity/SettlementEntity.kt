package ru.kpfu.itis.t_travel.data.local.database.entity

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
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["tripId"]),
        Index(value = ["id"])
    ]
)
data class SettlementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tripId: Int,
    val fromParticipantId: Int,
    val toParticipantId: Int,
    val amount: Double,
    val status: String,
    val lastUpdated: Long = System.currentTimeMillis()
) 