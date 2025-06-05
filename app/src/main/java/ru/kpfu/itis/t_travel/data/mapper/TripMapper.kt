package ru.kpfu.itis.t_travel.data.mapper

import ru.kpfu.itis.t_travel.data.local.database.entity.TripEntity
import ru.kpfu.itis.t_travel.data.model.TripDto
import ru.kpfu.itis.t_travel.domain.model.Trip

fun TripDto.toEntity(): TripEntity {
    return TripEntity(
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

fun TripEntity.toDomain(): Trip {
    return Trip(
        id = id,
        title = title,
        description = description,
        startDate = startDate,
        endDate = endDate,
        departureCity = departureCity,
        destinationCity = destinationCity,
        createdBy = createdBy,
    )
}

fun Trip.toEntity(): TripEntity {
    return TripEntity(
        id = id,
        title = title,
        description = description,
        startDate = startDate,
        endDate = endDate,
        departureCity = departureCity,
        destinationCity = destinationCity,
        createdBy = createdBy,
    )
}

fun Trip.toDto(): TripDto {
    return TripDto(
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