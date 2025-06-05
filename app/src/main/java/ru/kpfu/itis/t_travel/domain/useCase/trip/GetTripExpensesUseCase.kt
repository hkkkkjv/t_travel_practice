package ru.kpfu.itis.t_travel.domain.useCase.trip

import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import javax.inject.Inject

class GetTripExpensesUseCase @Inject constructor(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(tripId: Int): List<Expense> =
        tripRepository.getTripExpenses(tripId)
}