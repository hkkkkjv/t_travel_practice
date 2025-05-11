package ru.kpfu.itis.t_travel.data.repository

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kpfu.itis.t_travel.domain.model.Trip
import ru.kpfu.itis.t_travel.domain.model.User
import ru.kpfu.itis.t_travel.data.remote.ApiService
import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import java.time.LocalDate
import javax.inject.Inject

class TripRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TripRepository {
    override suspend fun getTrips(): ImmutableList<Trip> {
        return try {
            mockTrips
        } catch (e: Exception) {
            persistentListOf()
        }
        //apiService.getTrips().map { it.toDomain() }
    }


    private val mockTrips = persistentListOf(
        Trip(
            id = 1,
            title = "Отпуск в Сочи",
            startDate = LocalDate.now().plusDays(10),
            endDate = LocalDate.now().plusDays(17),
            participants = listOf(
                User(1, "user1", "Иван", "Иванов", "+79991112233", "ivan@mail.com", ""),
                User(2, "user2", "Мария", "Петрова", "+79992223344", "maria@mail.com", "")
            ),
            budget = 100000.0,
            expenses = listOf(
                Expense(1, "Билеты на поезд", 15000.0, 1, listOf(1, 2)),
                Expense(2, "Отель", 40000.0, 2, listOf(1, 2))
            ),
            departureCity = "Казань",
            destinationCity = "Сочи",
            createdBy = 1
        )
    )
}