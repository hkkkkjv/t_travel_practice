package ru.kpfu.itis.t_travel.domain.model

data class Participant(
    val id: Int,
    val tripId: Int,
    val name: String,
    val contact: String,
    val confirmed: Boolean
) {
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
