package ru.kpfu.itis.t_travel.data.repository

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.t_travel.data.local.database.dao.BudgetDao
import ru.kpfu.itis.t_travel.data.local.database.dao.ExpenseDao
import ru.kpfu.itis.t_travel.data.local.database.dao.ParticipantDao
import ru.kpfu.itis.t_travel.data.local.database.dao.SettlementDao
import ru.kpfu.itis.t_travel.data.local.database.dao.TripDao
import ru.kpfu.itis.t_travel.data.local.database.entity.ExpenseBeneficiaryEntity
import ru.kpfu.itis.t_travel.data.local.database.entity.ParticipantEntity
import ru.kpfu.itis.t_travel.data.mapper.toDomain
import ru.kpfu.itis.t_travel.data.mapper.toDto
import ru.kpfu.itis.t_travel.data.mapper.toEntity
import ru.kpfu.itis.t_travel.data.model.ParticipantPhoneRequest
import ru.kpfu.itis.t_travel.data.model.TripDto
import ru.kpfu.itis.t_travel.data.remote.ApiService
import ru.kpfu.itis.t_travel.di.qualifier.IoDispatcher
import ru.kpfu.itis.t_travel.domain.model.Budget
import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.domain.model.Settlement
import ru.kpfu.itis.t_travel.domain.model.Trip
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import ru.kpfu.itis.t_travel.utils.Constants.Cache.CACHE_TIMEOUT
import javax.inject.Inject

private const val TAG = "TripRepositoryImpl"

class TripRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val tripDao: TripDao,
    private val expenseDao: ExpenseDao,
    private val participantDao: ParticipantDao,
    private val settlementDao: SettlementDao,
    private val budgetDao: BudgetDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : TripRepository {
    private fun isCacheValid(lastUpdated: Long): Boolean {
        return System.currentTimeMillis() - lastUpdated < CACHE_TIMEOUT
    }

    override suspend fun getTrips(forceRefresh: Boolean): List<Trip> =
        withContext(dispatcher) {
            if (!forceRefresh) {
                val cachedTrips = getCachedTrips()
                if (cachedTrips.isNotEmpty()) {
                    Log.d(TAG, "Using cached trips for user")
                    return@withContext cachedTrips
                }
            }
            val trips = apiService.getTrips()
            val currentTime = System.currentTimeMillis()
            val resultTrips = mutableListOf<Trip>()

            for (tripDto in trips) {
                val tripId = tripDto.id
                val participants = apiService.getParticipants(tripId)
                saveTripToCache(
                    tripDto,
                    participants.map { it.toEntity().copy(lastUpdated = currentTime) },
                    currentTime
                )
                resultTrips.add(tripDto.toDomain())

            }
            Log.d(TAG, "Successfully fetched ${resultTrips.size} trips from API")
            resultTrips
        }

    private suspend fun getCachedTrips(): List<Trip> {
        val cachedTrips = tripDao.getAllTrips()
        if (cachedTrips.isEmpty() || !cachedTrips.all { isCacheValid(it.lastUpdated) }) {
            return emptyList()
        }

        return cachedTrips.map { it.toDomain() }
    }

    private suspend fun saveTripToCache(
        tripDto: TripDto,
        participants: List<ParticipantEntity>,
        currentTime: Long
    ) {
        try {
            tripDao.insertTrip(tripDto.toEntity().copy(lastUpdated = currentTime))
            participantDao.insertParticipants(participants)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving trip to cache", e)
        }
    }

    override suspend fun getTripDetails(tripId: Int, forceRefresh: Boolean): Trip {
        if (tripId == -1) {
            throw IllegalArgumentException("Invalid trip ID")
        }

        if (!forceRefresh) {
            val cachedTrip = getCachedTripDetails(tripId)
            if (cachedTrip != null) {
                Log.d(TAG, "Using cached trip details for trip $tripId")
                return cachedTrip
            }
        }

        val tripDto = apiService.getTripDetails(tripId)
        val currentTime = System.currentTimeMillis()
        saveTripToCache(tripDto, emptyList(), currentTime)
        Log.d(TAG, "Successfully fetched trip details for trip $tripId")
        return tripDto.toDomain()
    }

    private suspend fun getCachedTripDetails(tripId: Int): Trip? {
        return withContext(dispatcher) {
            val cachedTrip = tripDao.getTripById(tripId) ?: return@withContext null
            return@withContext if (isCacheValid(cachedTrip.lastUpdated)) {
                cachedTrip.toDomain()
            } else null
        }
    }

    override suspend fun getTripParticipants(
        tripId: Int,
        forceRefresh: Boolean
    ): List<Participant> {
        if (!forceRefresh) {
            val cachedParticipants = getCachedParticipants(tripId)
            if (cachedParticipants.isNotEmpty()) {
                Log.d(TAG, "Using cached participants for trip $tripId")
                return cachedParticipants
            }
        }

        val participants = apiService.getParticipants(tripId)
        val currentTime = System.currentTimeMillis()
        val participantEntities = participants.map {
            it.toEntity().copy(lastUpdated = currentTime)
        }
        try {
            participantDao.insertParticipants(participantEntities)
        } catch (e: Exception) {
            Log.e(TAG, "Error caching participants", e)
        }
        Log.d(TAG, "Successfully fetched ${participants.size} participants for trip $tripId")
        return participants.map { it.toDomain() }
    }

    private suspend fun getCachedParticipants(tripId: Int): List<Participant> {
        return withContext(dispatcher) {
            val cachedParticipants = participantDao.getParticipantsForTrip(tripId)
            return@withContext if (cachedParticipants.isNotEmpty() &&
                cachedParticipants.all { isCacheValid(it.lastUpdated) }
            ) {
                cachedParticipants.map { it.toDomain() }
            } else emptyList()
        }
    }

    override suspend fun confirmParticipation(tripId: Int, participantId: Int) {
        apiService.confirmParticipation(tripId, participantId)
        try {
            participantDao.updateConfirmationStatus(participantId, true)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating participant confirmation in cache", e)
        }
        Log.d(
            TAG,
            "Successfully confirmed participation for participant $participantId in trip $tripId"
        )
    }


    override suspend fun rejectParticipation(tripId: Int, participantId: Int) {
        apiService.rejectParticipation(tripId, participantId)
        try {
            participantDao.updateConfirmationStatus(participantId, false)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating participant rejection in cache", e)
        }
        Log.d(
            TAG,
            "Successfully rejected participation for participant $participantId in trip $tripId"
        )
    }

    override suspend fun getTripBudget(tripId: Int, forceRefresh: Boolean): Budget {
        if (!forceRefresh) {
            val cachedBudget = getCachedBudget(tripId)
            if (cachedBudget != null) {
                Log.d(TAG, "Using cached budget for trip $tripId")
                return cachedBudget
            }
        }

        val budgetDto = apiService.getBudget(tripId)
        val currentTime = System.currentTimeMillis()
        try {
            val budgetEntity = budgetDto.toEntity(tripId = tripId).copy(lastUpdated = currentTime)
            budgetDao.insertBudget(budgetEntity)
            val categoryEntities = budgetDto.categories.map {
                it.toEntity(tripId).copy(lastUpdated = currentTime)
            }
            budgetDao.insertBudgetCategories(categoryEntities)
        } catch (e: Exception) {
            Log.e(TAG, "Error caching budget", e)
        }
        Log.d(TAG, "Successfully fetched budget for trip $tripId")
        return budgetDto.toDomain(tripId = tripId)
    }

    private suspend fun getCachedBudget(tripId: Int): Budget? {
        return withContext(dispatcher) {
            val cachedBudget = budgetDao.getBudgetForTrip(tripId) ?: return@withContext null
            val cachedCategories = budgetDao.getBudgetCategoriesForTrip(tripId)

            return@withContext if (isCacheValid(cachedBudget.lastUpdated) &&
                cachedCategories.isNotEmpty() &&
                cachedCategories.all { isCacheValid(it.lastUpdated) }
            ) {
                cachedBudget.toDomain(cachedCategories.map { it.toDomain() })
            } else null
        }
    }

    override suspend fun getTripExpenses(tripId: Int, forceRefresh: Boolean): List<Expense> {
        if (!forceRefresh) {
            val cachedExpenses = getCachedExpenses(tripId)
            if (cachedExpenses.isNotEmpty()) {
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
                        participantId = participantId
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

    private suspend fun getCachedExpenses(tripId: Int): List<Expense> {
        return withContext(dispatcher) {
            val cachedExpenses = expenseDao.getExpensesForTrip(tripId)
            if (cachedExpenses.isEmpty() || !cachedExpenses.all { isCacheValid(it.lastUpdated) }) {
                return@withContext emptyList()
            }

            return@withContext cachedExpenses.map { expense ->
                val beneficiaries = expenseDao.getBeneficiariesForExpense(expense.id)
                expense.toDomain(beneficiaries.map { it.participantId })
            }
        }
    }

    override suspend fun getTripSettlements(tripId: Int, forceRefresh: Boolean): Settlement {
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
        return withContext(dispatcher) {
            val cachedSettlements = settlementDao.getSettlementsForTrip(tripId)
            return@withContext if (cachedSettlements.isNotEmpty() &&
                cachedSettlements.all { isCacheValid(it.lastUpdated) }
            ) {
                Settlement(cachedSettlements.map { it.toDomain() })
            } else null
        }
    }

    override suspend fun createTrip(trip: Trip): Trip {
        return apiService.createTrip(trip.toDto()).toDomain()
    }

    override suspend fun addParticipant(tripId: Int, participant: Participant): Participant {
        return apiService.addParticipant(tripId, ParticipantPhoneRequest(participant.contact))
            .toDomain()
    }

    override suspend fun setBudget(tripId: Int, budget: Budget) {
        apiService.setBudget(tripId, budget.toDto())
    }

    override suspend fun addExpense(tripId: Int, expense: Expense): Expense {
        return apiService.addExpense(tripId, expense.toDto()).toDomain()
    }
}