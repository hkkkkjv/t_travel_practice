package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName
import ru.kpfu.itis.t_travel.domain.model.AuthResult

data class AuthResponse(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("refreshExpiresIn") val refreshExpiresIn: Long,
    @SerializedName("tokenType") val tokenType: String,
    @SerializedName("expiresIn") val expiresIn: Long
) {

    fun toDomain(): AuthResult {
        return AuthResult.Success(
            token = accessToken,
            refreshToken = refreshToken,
            refreshExpiresIn = refreshExpiresIn,
            tokenType = tokenType,
            expiresIn = expiresIn,
        )
    }
}