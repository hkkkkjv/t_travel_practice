package ru.kpfu.itis.t_travel.presentation.common

interface ProfileManager {
    fun saveAvatarUri(uri: String)
    fun getAvatarUri(): String?
    fun clearAvatarUri()
}