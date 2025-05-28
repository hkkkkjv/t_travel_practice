package ru.kpfu.itis.t_travel.domain.useCase.trip

import ru.kpfu.itis.t_travel.domain.model.Trip
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import javax.inject.Inject

class GetTripDetailsUseCaseImpl @Inject constructor(
    private val tripRepository: TripRepository
) : GetTripDetailsUseCase {
    override suspend operator fun invoke(tripId: Int): Result<Trip> {
        return tripRepository.getTripDetails(tripId)
    }
}