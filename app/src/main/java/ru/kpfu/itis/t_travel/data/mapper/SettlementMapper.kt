package ru.kpfu.itis.t_travel.data.mapper

import ru.kpfu.itis.t_travel.data.local.database.entity.SettlementEntity
import ru.kpfu.itis.t_travel.data.model.SettlementDto
import ru.kpfu.itis.t_travel.data.model.SettlementItemDto
import ru.kpfu.itis.t_travel.domain.model.Settlement
import ru.kpfu.itis.t_travel.domain.model.SettlementItem

fun SettlementDto.toEntity(tripId: Int): List<SettlementEntity> {
    return settlements.map { settlement ->
        SettlementEntity(
            tripId = tripId,
            fromParticipantId = settlement.from,
            toParticipantId = settlement.to,
            amount = settlement.amount,
            status = settlement.status,
        )
    }
}

fun SettlementEntity.toDomain(): SettlementItem {
    return SettlementItem(
        from = fromParticipantId,
        to = toParticipantId,
        amount = amount,
        status = status,
    )
}

fun Settlement.toEntity(tripId: Int): List<SettlementEntity> {
    return settlements.map { settlement ->
        SettlementEntity(
            tripId = tripId,
            fromParticipantId = settlement.from,
            toParticipantId = settlement.to,
            amount = settlement.amount,
            status = settlement.status
        )
    }
}

fun Settlement.toDto(): SettlementDto {
    return SettlementDto(
        settlements = settlements.map { settlement ->
            SettlementItemDto(
                amount = settlement.amount,
                from = settlement.from,
                to = settlement.to,
                status = settlement.status,
            )
        }
    )
} 