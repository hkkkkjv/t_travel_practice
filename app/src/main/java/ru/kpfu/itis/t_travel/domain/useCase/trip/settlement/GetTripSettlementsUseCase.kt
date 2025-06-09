package ru.kpfu.itis.t_travel.domain.useCase.trip.settlement

import ru.kpfu.itis.t_travel.domain.model.Settlement
import ru.kpfu.itis.t_travel.domain.repository.SettlementRepository
import javax.inject.Inject

class GetTripSettlementsUseCase @Inject constructor(
    private val settlementRepository: SettlementRepository
) {
    suspend operator fun invoke(tripId: Int,forceRefresh:Boolean = true): Settlement =
        settlementRepository.getSettlements(tripId, forceRefresh = forceRefresh)
}