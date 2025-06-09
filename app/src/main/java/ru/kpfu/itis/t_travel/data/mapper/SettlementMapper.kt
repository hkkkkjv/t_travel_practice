package ru.kpfu.itis.t_travel.data.mapper

import ru.kpfu.itis.t_travel.data.local.database.entity.SettlementEntity
import ru.kpfu.itis.t_travel.data.model.SettlementDto
import ru.kpfu.itis.t_travel.data.model.SettlementItemDto
import ru.kpfu.itis.t_travel.domain.model.Settlement
import ru.kpfu.itis.t_travel.domain.model.SettlementItem

fun SettlementDto.toEntity(tripId: Int): List<SettlementEntity> {
    return settlements.map { settlement ->
        SettlementEntity(
            id = settlement.id,
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
        id = id,
        from = fromParticipantId,
        to = toParticipantId,
        amount = amount,
        status = status,
    )
}

fun SettlementDto.toDomain(): Settlement {
    return Settlement(settlements = settlements.map { it.toDomain() })
}

fun SettlementItemDto.toDomain(): SettlementItem {
    return SettlementItem(
        id = id,
        from = from,
        to = to,
        amount = amount,
        status = status
    )
}

fun Settlement.toEntity(tripId: Int): List<SettlementEntity> {
    return settlements.map { settlement ->
        SettlementEntity(
            id = settlement.id,
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
                id = settlement.id,
                amount = settlement.amount,
                from = settlement.from,
                to = settlement.to,
                status = settlement.status,
            )
        }
    )
} 