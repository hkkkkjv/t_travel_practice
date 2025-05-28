package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName

data class SettlementItemDto(
    @SerializedName("from") val from: String,
    @SerializedName("to") val to: String,
    @SerializedName("amount") val amount: Long
)

