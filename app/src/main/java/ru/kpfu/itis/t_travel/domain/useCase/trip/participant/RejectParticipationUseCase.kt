package ru.kpfu.itis.t_travel.domain.useCase.trip.participant

import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import javax.inject.Inject

class RejectParticipationUseCase @Inject constructor(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(tripId: Int): Unit {
        return tripRepository.rejectParticipation(tripId)
    }
}

