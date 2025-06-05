package ru.kpfu.itis.t_travel.domain.model

import ru.kpfu.itis.t_travel.data.model.ParticipantDto

data class Participant(
    val id: Int,
    val tripId: Int,
    val name: String,
    val contact: String,
    val confirmed: Boolean
) {
    fun toDto(): ParticipantDto {
        return ParticipantDto(
            tripId = tripId,
            name = name,
            contact = contact,
        )
    }

    companion object {
        fun mock(id: Int = 1) = Participant(
            id = id,
            tripId = 1,
            name = "Участник $id",
            contact = "+7999${1112233 + id}",
            confirmed = true
        )
    }
}
