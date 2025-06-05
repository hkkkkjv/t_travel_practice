package ru.kpfu.itis.t_travel.domain.model

import ru.kpfu.itis.t_travel.data.model.TripDto
import java.time.LocalDate

data class Trip(
    val id: Int,
    val title: String,
    val description: String?,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val departureCity: String,
    val destinationCity: String,
    val createdBy: Int = -1,
) {
    fun toDto(): TripDto {
        return TripDto(
            title = title,
            description = description,
            startDate = startDate,
            endDate = endDate,
            departureCity = departureCity,
            destinationCity = destinationCity,
            createdBy = createdBy
        )
    }

    companion object {
        fun mock(id: Int) = Trip(
            id = id,
            title = "Отпуск в Сочи",
            description = "Сочьники",
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(7),
            departureCity = "Казань",
            destinationCity = "Сочи",
            createdBy = id,
        )
    }
}
