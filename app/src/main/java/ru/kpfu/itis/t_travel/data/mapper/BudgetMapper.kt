package ru.kpfu.itis.t_travel.data.mapper

import ru.kpfu.itis.t_travel.data.local.database.entity.BudgetCategoryEntity
import ru.kpfu.itis.t_travel.data.local.database.entity.BudgetEntity
import ru.kpfu.itis.t_travel.data.model.BudgetCategoryDto
import ru.kpfu.itis.t_travel.data.model.BudgetDto
import ru.kpfu.itis.t_travel.domain.model.Budget
import ru.kpfu.itis.t_travel.domain.model.BudgetCategory

fun BudgetDto.toEntity(tripId: Int): BudgetEntity {
    return BudgetEntity(
        tripId = tripId,
        totalBudget = totalBudget
    )
}

fun BudgetEntity.toDomain(categories: List<BudgetCategory>): Budget {
    return Budget(
        tripId = tripId,
        totalBudget = totalBudget,
        categories = categories
    )
}

fun BudgetCategoryDto.toEntity(tripId: Int): BudgetCategoryEntity {
    return BudgetCategoryEntity(
        tripId = tripId,
        category = category,
        allocatedAmount = allocatedAmount
    )
}

fun BudgetCategoryEntity.toDomain(): BudgetCategory {
    return BudgetCategory(
        category = category,
        allocatedAmount = allocatedAmount
    )
}

fun Budget.toEntity(tripId: Int): BudgetEntity {
    return BudgetEntity(
        tripId = tripId,
        totalBudget = totalBudget,
    )
}

fun BudgetCategory.toEntity(tripId: Int): BudgetCategoryEntity {
    return BudgetCategoryEntity(
        tripId = tripId,
        category = category,
        allocatedAmount = allocatedAmount
    )
}

fun Budget.toDto(): BudgetDto {
    return BudgetDto(
        totalBudget = totalBudget,
        categories = categories.map { it.toDto() }
    )
}

fun BudgetCategory.toDto(): BudgetCategoryDto {
    return BudgetCategoryDto(
        category = category,
        allocatedAmount = allocatedAmount
    )
} 