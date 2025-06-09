package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName

data class DeviceTokenRequest(
    @SerializedName("deviceToken")val deviceToken: String
)