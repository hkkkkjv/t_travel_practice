package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName
import ru.kpfu.itis.t_travel.domain.model.User

data class UserDto(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("password") val password: String
) {
    fun toDomain(): User {
        return User(
            id = id,
            username = username,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            email = email,
            password = password
        )
    }
}
