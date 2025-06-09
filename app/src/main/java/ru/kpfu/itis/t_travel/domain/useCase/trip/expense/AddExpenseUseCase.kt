package ru.kpfu.itis.t_travel.domain.useCase.trip.expense

import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.repository.ExpenseRepository
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(tripId: Int, expense: Expense): Expense {
        return expenseRepository.addExpense(
            tripId,
            expense = expense
        )
    }
}
