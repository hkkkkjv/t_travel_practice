package ru.kpfu.itis.t_travel.domain.model

import java.time.LocalDate

data class Trip(
    val id: Int,
    val title: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val departureCity: String,
    val destinationCity: String,
    val participants: List<User>,
    val createdBy: Long,
    val budget: Double,
    val expenses: List<Expense>
){
    companion object {
        fun mock() = Trip(
            id = 1,
            title = "Отпуск в Сочи",
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(7),
            departureCity = "Казань",
            destinationCity = "Сочи",
            participants = listOf(User.mock()),
            createdBy = 1,
            budget = 100000.0,
            expenses = listOf(Expense.mock())
        )
    }
}
