package ru.kpfu.itis.t_travel.data.local.prefs

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.kpfu.itis.t_travel.presentation.common.settings.TokenManager
import javax.inject.Inject

class TokenManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenManager {
    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveTokens(access: String, refresh: String) {
        sharedPreferences.edit {
            putString(KEY_ACCESS_TOKEN, access).putString(
                KEY_REFRESH_TOKEN,
                refresh
            )
        }
    }

    override fun getAccessToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }

    override fun getRefreshToken(): String? {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
    }

    override fun clearTokens() {
        sharedPreferences.edit { remove(KEY_ACCESS_TOKEN).remove(KEY_REFRESH_TOKEN) }
    }

    fun saveAvatarUri(uri: String) {
        sharedPreferences.edit { putString(KEY_AVATAR_URI, uri) }
    }

    fun getAvatarUri(): String? = sharedPreferences.getString(KEY_AVATAR_URI, null)

    companion object {
        private const val KEY_AVATAR_URI = "avatar_uri"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val PREFS_NAME = "auth_prefs"
    }

}