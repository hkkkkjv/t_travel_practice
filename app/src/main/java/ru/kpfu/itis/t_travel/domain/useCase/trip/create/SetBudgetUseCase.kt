package ru.kpfu.itis.t_travel.domain.useCase.trip.create

import ru.kpfu.itis.t_travel.domain.model.Budget
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import javax.inject.Inject

class SetBudgetUseCase @Inject constructor(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(tripId: Int, budget: Budget) {
        return tripRepository.setBudget(tripId, budget)
    }
}