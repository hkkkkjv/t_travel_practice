package ru.kpfu.itis.t_travel.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "expense_beneficiaries",
    foreignKeys = [
        ForeignKey(
            entity = ExpenseEntity::class,
            parentColumns = ["id"],
            childColumns = ["expenseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ParticipantEntity::class,
            parentColumns = ["id"],
            childColumns = ["participantId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["expenseId"]),
        Index(value = ["participantId"]),
        Index(value = ["id"])
    ]
)
data class ExpenseBeneficiaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val expenseId: Int,
    val participantId: Int,
    val lastUpdated: Long = System.currentTimeMillis()
)