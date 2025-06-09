package ru.kpfu.itis.t_travel.data.repository

import android.util.Log
import ru.kpfu.itis.t_travel.data.local.database.dao.SettlementDao
import ru.kpfu.itis.t_travel.data.mapper.toDomain
import ru.kpfu.itis.t_travel.data.mapper.toEntity
import ru.kpfu.itis.t_travel.data.remote.ApiService
import ru.kpfu.itis.t_travel.domain.model.Settlement
import ru.kpfu.itis.t_travel.domain.model.SettlementItem
import ru.kpfu.itis.t_travel.domain.repository.SettlementRepository
import ru.kpfu.itis.t_travel.utils.Constants.Cache.CACHE_TIMEOUT
import javax.inject.Inject

class SettlementRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val settlementDao: SettlementDao
) : SettlementRepository {

    companion object {
        private const val TAG = "SettlementRepository"
    }

    override suspend fun getPayableSettlements(tripId: Int): List<SettlementItem> {
        return apiService.getPayableSettlements(tripId).map { it.toDomain() }.also {
            Log.d(TAG, "Получено ${it.size} долгов, где пользователь должен")
        }
    }

    override suspend fun getReceivableSettlements(tripId: Int): List<SettlementItem> {
        return apiService.getReceivableSettlements(tripId).map { it.toDomain() }.also {
            Log.d(TAG, "Получено ${it.size} долгов, где пользователю должны")
        }
    }

    override suspend fun requestDebtConfirmation(tripId: Int, settlementId: Int) {
        apiService.requestDebtConfirmation(tripId, settlementId)
        Log.d(
            TAG,
            "Запрос подтверждения долга отправлен для settlement $settlementId в поездке $tripId"
        )
    }

    override suspend fun confirmDebtReturn(tripId: Int, settlementId: Int) {
        apiService.confirmDebtReturn(tripId, settlementId)
        Log.d(TAG, "Возврат долга подтвержден для settlement $settlementId в поездке $tripId")
    }

    override suspend fun getSettlements(tripId: Int, forceRefresh: Boolean): Settlement {
        if (!forceRefresh) {
            val cachedSettlements = getCachedSettlements(tripId)
            if (cachedSettlements != null) {
                Log.d(TAG, "Using cached settlements for trip $tripId")
                return cachedSettlements
            }
        }
        val settlement = apiService.getSettlements(tripId).toDomain()
        try {
            settlementDao.insertSettlements(settlement.toEntity(tripId))
        } catch (e: Exception) {
            Log.e(TAG, "Error caching settlements", e)
        }
        Log.d(TAG, "Successfully fetched settlements for trip $tripId")
        return settlement
    }

    private suspend fun getCachedSettlements(tripId: Int): Settlement? {
        val cachedSettlements = settlementDao.getSettlementsForTrip(tripId)
        return if (cachedSettlements.isNotEmpty() &&
            cachedSettlements.all { isCacheValid(it.lastUpdated) }
        ) {
            Settlement(cachedSettlements.map { it.toDomain() })
        } else null
    }

    private fun isCacheValid(lastUpdated: Long): Boolean {
        return System.currentTimeMillis() - lastUpdated < CACHE_TIMEOUT
    }
}