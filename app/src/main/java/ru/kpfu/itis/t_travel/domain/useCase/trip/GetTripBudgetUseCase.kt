package ru.kpfu.itis.t_travel.domain.useCase.trip

import ru.kpfu.itis.t_travel.domain.model.Budget
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import javax.inject.Inject

class GetTripBudgetUseCase @Inject constructor(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(tripId: Int): Budget =
        tripRepository.getTripBudget(tripId)
}