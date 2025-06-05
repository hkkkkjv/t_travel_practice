package ru.kpfu.itis.t_travel.domain.model

import ru.kpfu.itis.t_travel.data.model.ExpenseDto

data class Expense(
    val id: Int = -1,
    val tripId: Int,
    val description: String,
    val amount: Double,
    val paidBy: Int = -1,
    val category: Int? = null,
    val beneficiaries: List<Int>
) {
    fun toDto(): ExpenseDto {
        return ExpenseDto(
            tripId = tripId,
            description = description,
            amount = amount,
            paidBy = paidBy,
            beneficiaries = beneficiaries
        )
    }

    companion object {
        fun mock() = Expense(
            id = 1,
            tripId = 1,
            description = "Билеты на самолёт",
            amount = 40000.0,
            paidBy = 1,
            beneficiaries = listOf(1, 2)
        )
    }
}