package ru.kpfu.itis.t_travel.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "participants",
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
data class ParticipantEntity(
    @PrimaryKey
    val id: Int,
    val tripId: Int,
    val name: String,
    val contact: String,
    val confirmed: Boolean,
    val lastUpdated: Long = System.currentTimeMillis()
) 