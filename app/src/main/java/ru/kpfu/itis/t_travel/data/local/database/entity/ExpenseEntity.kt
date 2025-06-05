package ru.kpfu.itis.t_travel.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ParticipantEntity::class,
            parentColumns = ["id"],
            childColumns = ["paidBy"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["tripId"]),
        Index(value = ["paidBy"]),
        Index(value = ["id"])
    ]
)
data class ExpenseEntity(
    @PrimaryKey
    val id: Int,
    val tripId: Int,
    val description: String,
    val amount: Double,
    val paidBy: Int,
    val lastUpdated: Long = System.currentTimeMillis()
)
