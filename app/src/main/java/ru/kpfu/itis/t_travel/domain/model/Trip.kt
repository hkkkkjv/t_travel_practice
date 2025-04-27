package ru.kpfu.itis.t_travel.domain.model

import java.time.LocalDate

data class Trip(
    val id: Int,
    val title: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val participants: List<User>,
    val budget: Double,
    val expenses: List<Expense>
){
    companion object {
        fun mock() = Trip(
            id = 1,
            title = "Отпуск в Сочи",
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(7),
            participants = listOf(User.mock()),
            budget = 100000.0,
            expenses = listOf(Expense.mock())
        )
    }
}
