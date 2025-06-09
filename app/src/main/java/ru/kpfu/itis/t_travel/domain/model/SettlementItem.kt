package ru.kpfu.itis.t_travel.domain.model

data class SettlementItem(
    val id: Int,
    val from: Int,
    val to: Int,
    val amount: Double,
    val status: String
)