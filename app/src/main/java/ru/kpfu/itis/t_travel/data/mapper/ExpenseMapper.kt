package ru.kpfu.itis.t_travel.data.mapper

import ru.kpfu.itis.t_travel.data.local.database.entity.ExpenseBeneficiaryEntity
import ru.kpfu.itis.t_travel.data.local.database.entity.ExpenseEntity
import ru.kpfu.itis.t_travel.data.model.ExpenseDto
import ru.kpfu.itis.t_travel.domain.model.Expense

fun ExpenseDto.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        tripId = tripId,
        description = description,
        amount = amount,
        paidBy = paidBy
    )
}

fun ExpenseEntity.toDomain(beneficiaries:List<Int>): Expense {
    return Expense(
        id = id,
        tripId = tripId,
        description = description,
        amount = amount,
        paidBy = paidBy,
        beneficiaries = beneficiaries
    )
}

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        tripId = tripId,
        description = description,
        amount = amount,
        paidBy = paidBy
    )
}

fun Expense.toDto(): ExpenseDto {
    return ExpenseDto(
        id = id,
        tripId = tripId,
        description = description,
        amount = amount,
        paidBy = paidBy,
        beneficiaries = beneficiaries
    )
}

fun ExpenseDto.toBeneficiaryEntities(): List<ExpenseBeneficiaryEntity> {
    return beneficiaries.map { participantId ->
        ExpenseBeneficiaryEntity(
            expenseId = id,
            participantId = participantId
        )
    }
} 