package ru.kpfu.itis.t_travel.data.repository

import retrofit2.HttpException
import ru.kpfu.itis.t_travel.data.model.BudgetCategoryDto
import ru.kpfu.itis.t_travel.data.model.BudgetDto
import ru.kpfu.itis.t_travel.data.remote.ApiService
import ru.kpfu.itis.t_travel.domain.model.BudgetCategory
import ru.kpfu.itis.t_travel.domain.model.Trip
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import javax.inject.Inject

class TripRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TripRepository {
    override suspend fun getTrips(): Result<List<Trip>> {
        return try {
            val tripsResponse = apiService.getTrips()
            if (tripsResponse.isSuccessful) {
                val trips = tripsResponse.body()
                    ?: return Result.failure(NullPointerException("Empty trips list"))
                val resultTrips = mutableListOf<Trip>()

                for (tripDto in trips) {
                    val tripId = tripDto.id
                    val participantsResponse = apiService.getParticipants(tripId)
                    val budgetResponse = apiService.getBudget(tripId)
                    val expensesResponse = apiService.getExpenses(tripId)
                    if (participantsResponse.isSuccessful &&
                        budgetResponse.isSuccessful &&
                        expensesResponse.isSuccessful
                    ) {
                        val participants = participantsResponse.body() ?: emptyList()
                        val budget = budgetResponse.body() ?: BudgetDto(0.0, listOf())
                        val expenses = expensesResponse.body() ?: emptyList()
                        resultTrips.add(
                            tripDto.toDomain(
                                participants = participants,
                                budget = budget,
                                expenses = expenses
                            )
                        )
                    } else {
                        return Result.failure(HttpException(participantsResponse))
                    }
                }
                Result.success(resultTrips)
            } else {
                Result.failure(HttpException(tripsResponse))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getTripDetails(tripId: Int): Result<Trip> {
        return try {
            if (tripId != -1) {
                val trip = apiService.getTripDetails(tripId)
                val participants = apiService.getParticipants(tripId)
                val budget = apiService.getBudget(tripId)
                val expenses = apiService.getExpenses(tripId)
                if (trip.isSuccessful && participants.isSuccessful && budget.isSuccessful && expenses.isSuccessful) {
                    val tripBody = trip.body()
                    val participantsBody = participants.body()
                    val budgetBody = budget.body()
                    val expensesBody = expenses.body()
                    if (tripBody != null && participantsBody != null && budgetBody != null && expensesBody != null) {
                        Result.success(
                            tripBody.toDomain(
                                participants = participantsBody,
                                budget = budgetBody,
                                expenses = expensesBody
                            )
                        )
                    } else {
                        Result.failure(NullPointerException())
                    }
                } else {
                    Result.failure(NullPointerException())
                }
            } else {
                Result.failure(Exception(""))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun confirmParticipation(tripId: Int, participantId: Int): Result<Unit> {
        return try {
            apiService.confirmParticipation(tripId, participantId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}