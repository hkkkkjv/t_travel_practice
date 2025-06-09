package ru.kpfu.itis.t_travel.utils

object Constants {
    object Validation {
        val PHONE_REGEX =
            "^(\\+?7)?[\\s\\-]?\\(?\\d{3}\\)?[\\s\\-]?\\d{3}[\\s\\-]?\\d{2}[\\s\\-]?\\d{2}\$".toRegex()
        val EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}\$".toRegex()
        val PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}\$".toRegex()
        val NAME_REGEX = "^[а-яА-Яa-zA-Z\\s\\-]+$".toRegex()
        val USERNAME_REGEX = "^[a-zA-Z0-9_.]+$".toRegex()
    }

    object Database {
        const val DATABASE_NAME = "t_travel_database"
    }

    object Cache {
        const val CACHE_TIMEOUT = 5 * 60 * 1000L
    }
}