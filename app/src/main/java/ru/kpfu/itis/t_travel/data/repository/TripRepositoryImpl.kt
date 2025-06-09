package ru.kpfu.itis.t_travel.data.repository

import android.util.Log
import ru.kpfu.itis.t_travel.data.local.database.dao.BudgetDao
import ru.kpfu.itis.t_travel.data.local.database.dao.ParticipantDao
import ru.kpfu.itis.t_travel.data.local.database.dao.TripDao
import ru.kpfu.itis.t_travel.data.local.database.entity.ParticipantEntity
import ru.kpfu.itis.t_travel.data.mapper.toDomain
import ru.kpfu.itis.t_travel.data.mapper.toDto
import ru.kpfu.itis.t_travel.data.mapper.toEntity
import ru.kpfu.itis.t_travel.data.model.ParticipantPhoneRequest
import ru.kpfu.itis.t_travel.data.model.TripDto
import ru.kpfu.itis.t_travel.data.remote.ApiService
import ru.kpfu.itis.t_travel.domain.model.Budget
import ru.kpfu.itis.t_travel.domain.model.BudgetCategoryLookup
import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.domain.model.Trip
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import ru.kpfu.itis.t_travel.utils.Constants.Cache.CACHE_TIMEOUT
import javax.inject.Inject

private const val TAG = "TripRepositoryImpl"

class TripRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val tripDao: TripDao,
    private val participantDao: ParticipantDao,
    private val budgetDao: BudgetDao,
) : TripRepository {
    private fun isCacheValid(lastUpdated: Long): Boolean {
        return System.currentTimeMillis() - lastUpdated < CACHE_TIMEOUT
    }

    override suspend fun getTrips(forceRefresh: Boolean): List<Trip> {
        if (!forceRefresh) {
            val cachedTrips = getCachedTrips()
            if (!cachedTrips.isNullOrEmpty()) {
                Log.d(TAG, "Using cached trips for user")
                return cachedTrips
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
        return resultTrips
    }


    private suspend fun getCachedTrips(): List<Trip>? {
        val cachedTrips = tripDao.getAllTrips()
        if (cachedTrips.isEmpty() || !cachedTrips.all { isCacheValid(it.lastUpdated) }) {
            return null
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
        val cachedTrip = tripDao.getTripById(tripId) ?: return null
        return if (isCacheValid(cachedTrip.lastUpdated)) {
            cachedTrip.toDomain()
        } else null
    }

    override suspend fun getTripParticipants(
        tripId: Int,
        forceRefresh: Boolean
    ): List<Participant> {
        if (!forceRefresh) {
            val cachedParticipants = getCachedParticipants(tripId)
            if (!cachedParticipants.isNullOrEmpty()) {
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

    private suspend fun getCachedParticipants(tripId: Int): List<Participant>? {
        val cachedParticipants = participantDao.getParticipantsForTrip(tripId)
        return if (cachedParticipants.isNotEmpty() &&
            cachedParticipants.all { isCacheValid(it.lastUpdated) }
        ) {
            cachedParticipants.map { it.toDomain() }
        } else null
    }

    override suspend fun confirmParticipation(tripId: Int) {
        apiService.confirmParticipation(tripId)
        try {
            val participants = getTripParticipants(tripId, true)
            participantDao.insertParticipants(participants.map { it.toEntity() })
        } catch (e: Exception) {
            Log.e(TAG, "Error updating participant confirmation in cache", e)
        }
        Log.d(
            TAG,
            "Successfully confirmed participation for participant in trip $tripId"
        )
    }


    override suspend fun rejectParticipation(tripId: Int) {
        apiService.rejectParticipation(tripId)
        try {
            val participants = getTripParticipants(tripId, true)
            participantDao.insertParticipants(participants.map { it.toEntity() })        } catch (e: Exception) {
            Log.e(TAG, "Error updating participant rejection in cache", e)
        }
        Log.d(
            TAG,
            "Successfully rejected participation for participant in trip $tripId"
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
        val cachedBudget = budgetDao.getBudgetForTrip(tripId) ?: return null
        val cachedCategories = budgetDao.getBudgetCategoriesForTrip(tripId)

        return if (isCacheValid(cachedBudget.lastUpdated) &&
            cachedCategories.isNotEmpty() &&
            cachedCategories.all { isCacheValid(it.lastUpdated) }
        ) {
            cachedBudget.toDomain(cachedCategories.map { it.toDomain() })
        } else null
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

    override suspend fun getPendingTrips(forceRefresh: Boolean): List<Trip> {
        if (!forceRefresh) {
            val cachedTrips = getCachedTrips()
            if (!cachedTrips.isNullOrEmpty()) {
                Log.d(TAG, "Using cached pending trips")
                return cachedTrips
            }
        }

        val trips = apiService.getPendingTrips()
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
        Log.d(TAG, "Successfully fetched ${resultTrips.size} pending trips from API")
        return resultTrips
    }

    override suspend fun getConfirmedTrips(forceRefresh: Boolean): List<Trip> {
        if (!forceRefresh) {
            val cachedTrips = getCachedTrips()
            if (!cachedTrips.isNullOrEmpty()) {
                Log.d(TAG, "Using cached confirmed trips")
                return cachedTrips
            }
        }

        val trips = apiService.getConfirmedTrips()
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
        Log.d(TAG, "Successfully fetched ${resultTrips.size} confirmed trips from API")
        return resultTrips
    }

    override suspend fun getBudgetCategories(): List<BudgetCategoryLookup> {
        return apiService.getBudgetCategories().map { it.toDomain() }
    }
}