package ru.kpfu.itis.t_travel.domain.model

import java.time.LocalDate

data class Trip(
    val id: Int,
    val title: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val departureCity: String,
    val destinationCity: String,
    val participants: List<Participant> = emptyList(),
    val createdBy: Int,
    val budget: Budget,
    val expenses: List<Expense>
) {
    companion object {
        fun mock(id: Int) = Trip(
            id = id,
            title = "Отпуск в Сочи",
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(7),
            departureCity = "Казань",
            destinationCity = "Сочи",
            participants = listOf(
                Participant.mock(1),
                Participant.mock(2)
            ),
            createdBy = id,
            budget = Budget.mock(15000.0),
            expenses = listOf(Expense.mock())
        )
    }
}
