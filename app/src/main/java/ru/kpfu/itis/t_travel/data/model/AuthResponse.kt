package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName
import ru.kpfu.itis.t_travel.domain.model.AuthResult

data class AuthResponse(
    @SerializedName("token") val token: String,
    @SerializedName("refreshToken") val refreshToken: String? = null,
    @SerializedName("userId") val userId: Int,
    @SerializedName("expiresIn") val expiresIn: Long? = null
) {

    fun toDomain(): AuthResult {
        return AuthResult.Success(
            token = token,
            refreshToken = refreshToken,
            userId = userId
        )
    }
}