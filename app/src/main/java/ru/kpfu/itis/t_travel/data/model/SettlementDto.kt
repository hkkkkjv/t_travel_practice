package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName

data class SettlementDto(
    @SerializedName("settlements") val settlements: List<SettlementItemDto>
)
