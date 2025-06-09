package ru.kpfu.itis.t_travel.data.local.database.entity

import androidx.room.ColumnInfo
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
            childColumns = ["trip_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["trip_id"]),
        Index(value = ["id"])
    ]
)
data class ParticipantEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "trip_id")
    val tripId: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "contact")
    val contact: String,
    @ColumnInfo(name = "confirmed")
    val confirmed: Boolean,
    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long = System.currentTimeMillis()
) 