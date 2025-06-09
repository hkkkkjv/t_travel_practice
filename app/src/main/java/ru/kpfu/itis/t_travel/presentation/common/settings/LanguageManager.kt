package ru.kpfu.itis.t_travel.presentation.common.settings

interface LanguageManager {
    fun saveLanguage(lang: String)
    fun getLanguage(): String
}
