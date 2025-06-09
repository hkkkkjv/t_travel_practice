package ru.kpfu.itis.t_travel.domain.useCase.trip.create

import ru.kpfu.itis.t_travel.domain.model.Trip
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import javax.inject.Inject

class CreateTripUseCase @Inject constructor(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(trip: Trip): Trip {
        return tripRepository.createTrip(trip)
    }
}
