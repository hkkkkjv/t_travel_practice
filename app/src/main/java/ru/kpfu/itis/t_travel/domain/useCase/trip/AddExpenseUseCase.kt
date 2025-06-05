package ru.kpfu.itis.t_travel.domain.useCase.trip

import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(tripId: Int, expense: Expense): Expense {
        return tripRepository.addExpense(
            tripId,
            expense = expense
        )
    }
}
