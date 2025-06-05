package ru.kpfu.itis.t_travel.data.local.prefs

import android.content.Context
import androidx.core.content.edit
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.kpfu.itis.t_travel.presentation.common.AvatarManager
import ru.kpfu.itis.t_travel.presentation.common.ProfileManager
import javax.inject.Inject

class ProfileManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val avatarManager: AvatarManager

) : ProfileManager {
    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "profile_prefs"
        private const val KEY_AVATAR_PATH = "avatar_path"
    }

    override fun saveAvatarUri(uri: String) {
        val path = avatarManager.saveAvatar(uri.toUri())
        sharedPreferences.edit { putString(KEY_AVATAR_PATH, path) }
    }

    override fun getAvatarUri(): String? {
        val path = sharedPreferences.getString(KEY_AVATAR_PATH, null)
        return avatarManager.getAvatarUri(path)?.toString()
    }

    override fun clearAvatarUri() {
        val path = sharedPreferences.getString(KEY_AVATAR_PATH, null)
        avatarManager.clearAvatar(path)
        sharedPreferences.edit { remove(KEY_AVATAR_PATH) }
    }
}