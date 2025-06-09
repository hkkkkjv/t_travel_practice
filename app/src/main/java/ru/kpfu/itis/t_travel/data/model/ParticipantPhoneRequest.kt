package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName

data class ParticipantPhoneRequest(
    @SerializedName("phone") val phone: String,
)