package ru.kpfu.itis.t_travel.data.local.prefs

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.kpfu.itis.t_travel.presentation.common.ThemeManager
import javax.inject.Inject

class ThemeManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ThemeManager {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    override fun saveTheme(theme: String) = prefs.edit { putString(KEY_THEME, theme) }
    override fun getTheme(): String = prefs.getString(KEY_THEME, DEFAULT_VALUE) ?: DEFAULT_VALUE

    companion object {
        private const val PREFS_NAME = "settings_prefs"
        private const val KEY_THEME = "theme"
        private const val DEFAULT_VALUE = "SYSTEM"
    }
}
