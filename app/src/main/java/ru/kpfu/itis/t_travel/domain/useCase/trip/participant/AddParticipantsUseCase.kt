package ru.kpfu.itis.t_travel.domain.useCase.trip.participant

import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import javax.inject.Inject

class AddParticipantsUseCase @Inject constructor(
    private val tripRepository: TripRepository
){
    suspend operator fun invoke(tripId: Int,participant: Participant ): Participant {
        return tripRepository.addParticipant(
            tripId,
            participant = participant
        )
    }
}


