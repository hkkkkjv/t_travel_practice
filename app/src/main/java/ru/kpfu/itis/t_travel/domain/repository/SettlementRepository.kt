package ru.kpfu.itis.t_travel.domain.repository

import ru.kpfu.itis.t_travel.domain.model.Settlement
import ru.kpfu.itis.t_travel.domain.model.SettlementItem

interface SettlementRepository {
    suspend fun getSettlements(tripId: Int, forceRefresh: Boolean): Settlement
    suspend fun getPayableSettlements(tripId: Int): List<SettlementItem>
    suspend fun getReceivableSettlements(tripId: Int): List<SettlementItem>
    suspend fun requestDebtConfirmation(tripId: Int, settlementId: Int)
    suspend fun confirmDebtReturn(tripId: Int, settlementId: Int)
}