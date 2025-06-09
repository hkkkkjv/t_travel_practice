package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName

data class SettlementItemDto(
    @SerializedName("id") val id: Int,
    @SerializedName("from") val from: Int,
    @SerializedName("to") val to: Int,
    @SerializedName("amount") val amount: Double,
    @SerializedName("status") val status: String,
)

