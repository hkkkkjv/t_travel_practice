package ru.kpfu.itis.t_travel.domain.model

import java.time.OffsetDateTime

data class Notification(
    val id: Int,
    val message: String,
    val date: OffsetDateTime
)