package ru.kpfu.itis.t_travel.domain.useCase.trip.expense

import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.repository.ExpenseRepository
import javax.inject.Inject

class GetMyExpensesUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(tripId: Int): List<Expense> {
        return expenseRepository.getMyExpenses(tripId)
    }
}