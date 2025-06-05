package ru.kpfu.itis.t_travel.data.mapper

import ru.kpfu.itis.t_travel.data.local.database.entity.ParticipantEntity
import ru.kpfu.itis.t_travel.data.model.ParticipantDto
import ru.kpfu.itis.t_travel.domain.model.Participant

fun ParticipantDto.toEntity(): ParticipantEntity {
    return ParticipantEntity(
        id = id,
        tripId = tripId,
        name = name,
        contact = contact,
        confirmed = confirmed
    )
}

fun ParticipantEntity.toDomain(): Participant {
    return Participant(
        id = id,
        tripId = tripId,
        name = name,
        contact = contact,
        confirmed = confirmed
    )
}

fun Participant.toEntity(): ParticipantEntity {
    return ParticipantEntity(
        id = id,
        tripId = tripId,
        name = name,
        contact = contact,
        confirmed = confirmed
    )
}

fun Participant.toDto(): ParticipantDto {
    return ParticipantDto(
        id = id,
        tripId = tripId,
        name = name,
        contact = contact,
        confirmed = confirmed
    )
}