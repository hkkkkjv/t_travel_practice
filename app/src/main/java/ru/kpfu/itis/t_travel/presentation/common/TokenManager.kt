package ru.kpfu.itis.t_travel.presentation.common

interface TokenManager {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
}