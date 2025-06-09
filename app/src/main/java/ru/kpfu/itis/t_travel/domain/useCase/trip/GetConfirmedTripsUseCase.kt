package ru.kpfu.itis.t_travel.domain.useCase.trip

import ru.kpfu.itis.t_travel.domain.model.Trip
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import javax.inject.Inject

class GetConfirmedTripsUseCase @Inject constructor(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(forceRefresh: Boolean = true): List<Trip> =
        tripRepository.getConfirmedTrips(forceRefresh = forceRefresh)
}