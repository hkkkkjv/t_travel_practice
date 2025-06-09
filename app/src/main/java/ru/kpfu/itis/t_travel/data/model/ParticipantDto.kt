package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName
import ru.kpfu.itis.t_travel.domain.model.Participant

data class ParticipantDto(
    @SerializedName("id") val id: Int = -1,
    @SerializedName("tripId") val tripId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("contact") val contact: String,
    @SerializedName("confirmed") val confirmed: Boolean = false
) {
    fun toDomain(): Participant {
        return Participant(
            id = id,
            tripId = tripId,
            name = name,
            contact = contact,
            confirmed = confirmed
        )
    }
}
