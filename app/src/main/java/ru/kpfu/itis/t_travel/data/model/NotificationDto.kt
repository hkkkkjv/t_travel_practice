package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName

data class NotificationDto(
    @SerializedName("id") val id: Long,
    @SerializedName("userId") val userId: Long,
    @SerializedName("message") val message: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("read") val read: Boolean
)