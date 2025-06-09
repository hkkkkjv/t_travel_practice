package ru.kpfu.itis.t_travel.domain.useCase.trip.settlement

import ru.kpfu.itis.t_travel.domain.repository.SettlementRepository
import javax.inject.Inject

class RequestDebtConfirmationUseCase @Inject constructor(
    private val settlementRepository: SettlementRepository
) {
    suspend operator fun invoke(tripId: Int, settlementId: Int) {
        settlementRepository.requestDebtConfirmation(tripId, settlementId)
    }
}