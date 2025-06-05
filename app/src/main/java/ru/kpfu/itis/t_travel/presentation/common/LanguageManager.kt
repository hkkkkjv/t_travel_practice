package ru.kpfu.itis.t_travel.presentation.common

interface LanguageManager {
    fun saveLanguage(lang: String)
    fun getLanguage(): String
}
