package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName
import ru.kpfu.itis.t_travel.domain.model.Settlement

data class SettlementDto(
    @SerializedName("settlements") val settlements: List<SettlementItemDto>
) {
    fun toDomain(): Settlement {
        return Settlement(
            settlements = settlements.map { it.toDomain() }
        )
    }
}
