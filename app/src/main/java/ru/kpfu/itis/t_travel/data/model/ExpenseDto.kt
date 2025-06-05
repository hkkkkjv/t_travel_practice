package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName
import ru.kpfu.itis.t_travel.domain.model.Expense

data class ExpenseDto(
    @SerializedName("id") val id: Int,
    @SerializedName("tripId") val tripId: Long,
    @SerializedName("description") val description: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("paidBy") val paidBy: Int,
    @SerializedName("beneficiaries") val beneficiaries: List<Int>
) {
    fun toDomain(): Expense {
        return Expense(
            id = id,
            tripId = tripId,
            description = description,
            amount = amount,
            paidBy = paidBy,
            beneficiaries = beneficiaries,
        )
    }
}