package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName
import ru.kpfu.itis.t_travel.domain.model.Trip
import java.time.LocalDate

data class TripDto(
    @SerializedName("id") val id: Int = -1,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("startDate") val startDate: LocalDate,
    @SerializedName("endDate") val endDate: LocalDate,
    @SerializedName("departureCity") val departureCity: String,
    @SerializedName("destinationCity") val destinationCity: String,
    @SerializedName("createdBy") val createdBy: Int,
) {
    fun toDomain(): Trip {
        return Trip(
            id = id,
            title = title,
            description = description,
            startDate = startDate,
            endDate = endDate,
            departureCity = departureCity,
            destinationCity = destinationCity,
            createdBy = createdBy
        )
    }
}
