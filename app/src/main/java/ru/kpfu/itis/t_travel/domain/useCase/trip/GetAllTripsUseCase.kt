package ru.kpfu.itis.t_travel.domain.useCase.trip

import ru.kpfu.itis.t_travel.domain.model.Trip
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import javax.inject.Inject

class GetAllTripsUseCase @Inject constructor(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(): Result<List<Trip>> {
        return tripRepository.getTrips()
    }
}
