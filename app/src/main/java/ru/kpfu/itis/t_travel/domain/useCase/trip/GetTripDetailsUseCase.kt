package ru.kpfu.itis.t_travel.domain.useCase.trip

import ru.kpfu.itis.t_travel.domain.model.Trip

interface GetTripDetailsUseCase {
    suspend operator fun invoke(tripId: Int): Result<Trip>
}
