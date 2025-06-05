package ru.kpfu.itis.t_travel.presentation.common

interface TokenManager {
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun saveTokens(access: String, refresh: String)
    fun clearTokens()
}