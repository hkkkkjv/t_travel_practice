package ru.kpfu.itis.t_travel.presentation.common

interface ThemeManager {
    fun saveTheme(theme: String)
    fun getTheme(): String
}
