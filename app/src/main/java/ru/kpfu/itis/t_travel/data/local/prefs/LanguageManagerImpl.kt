package ru.kpfu.itis.t_travel.data.local.prefs

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.kpfu.itis.t_travel.presentation.common.settings.LanguageManager
import javax.inject.Inject

class LanguageManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LanguageManager {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    override fun saveLanguage(lang: String) {
        Log.i("LanguageManagerImpl", "$lang")
        prefs.edit { putString(KEY_LANGUAGE, lang) }
    }

    override fun getLanguage(): String {
        Log.i("LanguageManagerImpl", "${prefs.getString(KEY_LANGUAGE, DEFAULT_VALUE)}")
        return prefs.getString(KEY_LANGUAGE, DEFAULT_VALUE) ?: DEFAULT_VALUE
    }

    companion object {
        private const val PREFS_NAME = "settings_prefs"
        private const val KEY_LANGUAGE = "lang"
        private const val DEFAULT_VALUE = "ru"
    }
}