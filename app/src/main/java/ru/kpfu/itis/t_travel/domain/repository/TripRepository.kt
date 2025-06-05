package ru.kpfu.itis.t_travel.domain.repository

import ru.kpfu.itis.t_travel.domain.model.Budget
import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.domain.model.Settlement
import ru.kpfu.itis.t_travel.domain.model.Trip

interface TripRepository {
    suspend fun getTrips( forceRefresh: Boolean = false): List<Trip>
    suspend fun getTripDetails(tripId: Int, forceRefresh: Boolean = false): Trip
    suspend fun confirmParticipation(tripId: Int, participantId: Int)
    suspend fun rejectParticipation(tripId: Int, participantId: Int)
    suspend fun getTripParticipants(
        tripId: Int,
        forceRefresh: Boolean = false
    ): List<Participant>

    suspend fun getTripBudget(tripId: Int, forceRefresh: Boolean = false): Budget
    suspend fun getTripExpenses(tripId: Int, forceRefresh: Boolean = false): List<Expense>
    suspend fun getTripSettlements(tripId: Int, forceRefresh: Boolean = false): Settlement
    suspend fun createTrip(trip: Trip): Trip
    suspend fun addParticipant(tripId: Int, participant: Participant): Participant
    suspend fun setBudget(tripId: Int, budget: Budget)
    suspend fun addExpense(tripId: Int, expense: Expense): Expense
}