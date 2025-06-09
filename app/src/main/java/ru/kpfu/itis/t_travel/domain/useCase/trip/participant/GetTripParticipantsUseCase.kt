package ru.kpfu.itis.t_travel.domain.useCase.trip.participant

import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import javax.inject.Inject

class GetTripParticipantsUseCase @Inject constructor(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(tripId: Int,forceRefresh :Boolean): List<Participant> =
        tripRepository.getTripParticipants(tripId, forceRefresh = forceRefresh)
}