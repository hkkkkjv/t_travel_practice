package ru.kpfu.itis.t_travel.data.repository

import android.util.Log
import ru.kpfu.itis.t_travel.data.local.database.dao.ExpenseDao
import ru.kpfu.itis.t_travel.data.local.database.entity.ExpenseBeneficiaryEntity
import ru.kpfu.itis.t_travel.data.mapper.toDomain
import ru.kpfu.itis.t_travel.data.mapper.toDto
import ru.kpfu.itis.t_travel.data.mapper.toEntity
import ru.kpfu.itis.t_travel.data.remote.ApiService
import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.repository.ExpenseRepository
import ru.kpfu.itis.t_travel.utils.Constants.Cache.CACHE_TIMEOUT
import javax.inject.Inject

private const val TAG = "ExpenseRepositoryImpl"

class ExpenseRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val expenseDao: ExpenseDao,
) : ExpenseRepository {
    private fun isCacheValid(lastUpdated: Long): Boolean {
        return System.currentTimeMillis() - lastUpdated < CACHE_TIMEOUT
    }

    override suspend fun addExpense(tripId: Int, expense: Expense): Expense {
        return apiService.addExpense(tripId, expense.toDto()).toDomain()
    }

    override suspend fun getTripExpenses(tripId: Int, forceRefresh: Boolean): List<Expense> {
        if (!forceRefresh) {
            val cachedExpenses = getCachedExpenses(tripId)
            if (!cachedExpenses.isNullOrEmpty()) {
                Log.d(TAG, "Using cached expenses for trip $tripId")
                return cachedExpenses
            }
        }

        val expenses = apiService.getExpenses(tripId).map { it.toDomain() }
        val currentTime = System.currentTimeMillis()
        try {
            val expenseEntities = expenses.map { it.toEntity() }
            expenseDao.insertExpenses(expenseEntities)

            val beneficiaryEntities = expenses.flatMap { expense ->
                expense.beneficiaries.map { participantId ->
                    ExpenseBeneficiaryEntity(
                        expenseId = expense.id,
                        participantId = participantId,
                        lastUpdated = currentTime
                    )
                }
            }
            expenseDao.insertBeneficiaries(beneficiaryEntities)
        } catch (e: Exception) {
            Log.e(TAG, "Error caching expenses", e)
        }
        Log.d(TAG, "Successfully fetched ${expenses.size} expenses for trip $tripId")
        return expenses
    }

    private suspend fun getCachedExpenses(tripId: Int): List<Expense>? {
        val cachedExpenses = expenseDao.getExpensesForTrip(tripId)
        if (cachedExpenses.isEmpty() || !cachedExpenses.all { isCacheValid(it.lastUpdated) }) {
            return null
        }
        return cachedExpenses.map { expense ->
            val beneficiaries = expenseDao.getBeneficiariesForExpense(expense.id)
            expense.toDomain(beneficiaries.map { it.participantId })
        }
    }

    override suspend fun getMyExpenses(tripId: Int): List<Expense> {
        return apiService.getMyExpenses(tripId).map { it.toDomain() }
    }

    override suspend fun deleteExpense(tripId: Int, expenseId: Int) {
        TODO("Not yet implemented")
    }

}
