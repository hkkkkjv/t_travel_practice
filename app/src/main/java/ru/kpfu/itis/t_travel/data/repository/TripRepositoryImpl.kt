package ru.kpfu.itis.t_travel.data.repository

import ru.kpfu.itis.t_travel.domain.model.Trip
import ru.kpfu.itis.t_travel.domain.model.User
import ru.kpfu.itis.t_travel.data.remote.ApiService
import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.model.LoginCredentials
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import java.time.LocalDate
import javax.inject.Inject

class TripRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TripRepository {
    override suspend fun getTrips(): List<Trip> {
        return try {
            mockTrips
        } catch (e: Exception) {
            emptyList()
        }
        //apiService.getTrips().map { it.toDomain() }
    }

    override suspend fun register(user: User) = apiService.register(user)
    override suspend fun login(credentials: LoginCredentials) = apiService.login(credentials)

    private val mockTrips = listOf(
        Trip(
            id = 1,
            title = "Отпуск в Сочи",
            startDate = LocalDate.now().plusDays(10),
            endDate = LocalDate.now().plusDays(17),
            participants = listOf(
                User(1, "user1", "Иван Иванов", "+79991112233", "ivan@mail.com", ""),
                User(2, "user2", "Мария Петрова", "+79992223344", "maria@mail.com", "")
            ),
            budget = 100000.0,
            expenses = listOf(
                Expense(1, "Билеты на поезд", 15000.0, 1, listOf(1, 2)),
                Expense(2, "Отель", 40000.0, 2, listOf(1, 2))
            )
        )
    )
}