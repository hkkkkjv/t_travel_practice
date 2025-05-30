package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName
import ru.kpfu.itis.t_travel.domain.model.Trip
import java.time.LocalDate

data class TripDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("startDate") val startDate: LocalDate,
    @SerializedName("endDate") val endDate: LocalDate,
    @SerializedName("departureCity") val departureCity: String,
    @SerializedName("destinationCity") val destinationCity: String,
    @SerializedName("createdBy") val createdBy: Long,
    @SerializedName("participants") val participants: List<UserDto>,
    @SerializedName("budget") val budget: Double,
    @SerializedName("expenses") val expenses: List<ExpenseDto>
) {
    fun toDomain(): Trip {
        return Trip(
            id = id,
            title = title,
            startDate = startDate,
            endDate = endDate,
            participants = participants.map { it.toDomain() },
            budget = budget,
            expenses = expenses.map { it.toDomain() },
            departureCity = TODO(),
            destinationCity = TODO(),
            createdBy = TODO()
        )
    }
}
