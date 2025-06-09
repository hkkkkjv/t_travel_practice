package ru.kpfu.itis.t_travel.domain.useCase.trip.expense

import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.repository.ExpenseRepository
import javax.inject.Inject

class GetTripExpensesUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(tripId: Int, forceRefresh: Boolean = true): List<Expense> =
        expenseRepository.getTripExpenses(tripId, forceRefresh = forceRefresh)
}