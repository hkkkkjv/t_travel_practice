package ru.kpfu.itis.t_travel.presentation.common.settings

interface ProfileManager {
    fun saveAvatarUri(uri: String)
    fun getAvatarUri(): String?
    fun clearAvatarUri()
}