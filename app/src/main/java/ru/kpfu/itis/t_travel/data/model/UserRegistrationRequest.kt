package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName

data class UserRegistrationRequest(
    @SerializedName("username") val username: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("password") val password: String
)
