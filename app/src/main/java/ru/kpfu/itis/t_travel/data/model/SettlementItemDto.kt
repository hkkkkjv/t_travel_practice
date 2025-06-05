package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName
import ru.kpfu.itis.t_travel.domain.model.SettlementItem

data class SettlementItemDto(
    @SerializedName("from") val from: Int,
    @SerializedName("to") val to: Int,
    @SerializedName("amount") val amount: Double,
    @SerializedName("status") val status: String,
){
    fun toDomain():SettlementItem{
        return SettlementItem(
            from = from,
            to = to,
            amount = amount,
            status = status
        )
    }
}

