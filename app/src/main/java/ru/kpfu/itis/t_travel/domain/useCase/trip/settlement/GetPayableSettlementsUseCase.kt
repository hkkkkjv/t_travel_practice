package ru.kpfu.itis.t_travel.domain.useCase.trip.settlement

import ru.kpfu.itis.t_travel.domain.model.SettlementItem
import ru.kpfu.itis.t_travel.domain.repository.SettlementRepository
import javax.inject.Inject

class GetPayableSettlementsUseCase @Inject constructor(
    private val settlementRepository: SettlementRepository
) {
    suspend operator fun invoke(tripId: Int): List<SettlementItem> {
        return settlementRepository.getPayableSettlements(tripId)
    }
}