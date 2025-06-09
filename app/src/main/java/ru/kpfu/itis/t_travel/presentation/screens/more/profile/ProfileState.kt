package ru.kpfu.itis.t_travel.presentation.screens.more.profile

import android.net.Uri
import androidx.compose.runtime.Immutable
import ru.kpfu.itis.t_travel.domain.model.User
import ru.kpfu.itis.t_travel.presentation.common.settings.AppLanguage
import ru.kpfu.itis.t_travel.presentation.common.settings.AppTheme

@Immutable
data class ProfileState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val avatarUri: Uri? = null,
    val theme: AppTheme = AppTheme.SYSTEM,
    val language: AppLanguage = AppLanguage.ru,
    val showThemeDialog: Boolean = false,
    val showLanguageDialog: Boolean = false,
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val email: String = "",
    val isSaving: Boolean = false
)