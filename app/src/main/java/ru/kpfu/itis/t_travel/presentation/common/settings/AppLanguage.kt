package ru.kpfu.itis.t_travel.presentation.common.settings

import java.util.Locale

enum class AppLanguage(val displayName: String, val locale: Locale) {
    ru("Русский", Locale("ru")),
    en("English", Locale.ENGLISH)
}