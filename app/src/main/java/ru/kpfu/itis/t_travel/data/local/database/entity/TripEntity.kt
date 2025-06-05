package ru.kpfu.itis.t_travel.data.local.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "trips",
    indices = [
        Index(value = ["createdBy"])
    ]
)data class TripEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String?,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val departureCity: String,
    val destinationCity: String,
    val createdBy: Int,
    val isFavorite: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
) 