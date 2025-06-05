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
    @SerializedName("createdBy") val createdBy: Int,
//    @SerializedName("participants") val participants: List<ParticipantDto>,
//    @SerializedName("budget") val budget: Double,
//    @SerializedName("expenses") val expenses: List<ExpenseDto>
) {
    fun toDomain(
        participants: List<ParticipantDto>,
        budget: BudgetDto,
        expenses: List<ExpenseDto>
    ): Trip {
//        val parsedStartDate = try { LocalDate.parse(startDate) } catch (e: Exception) { LocalDate.MIN }
//        val parsedEndDate = try { LocalDate.parse(endDate) } catch (e: Exception) { LocalDate.MIN }
        return Trip(
            id = id,
            title = title,
            startDate = startDate,
            endDate = endDate,
            participants = participants.map { it.toDomain() },//mapper
            budget = budget.toDomain(),
            expenses = expenses.map { it.toDomain() },
            departureCity = departureCity,
            destinationCity = destinationCity,
            createdBy = createdBy
        )
    }
}
