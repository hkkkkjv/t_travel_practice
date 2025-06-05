package ru.kpfu.itis.t_travel.presentation.screens.more.profile

import android.net.Uri
import ru.kpfu.itis.t_travel.presentation.common.AppLanguage
import ru.kpfu.itis.t_travel.presentation.common.AppTheme

sealed class ProfileEvent {
    object EditProfileClicked : ProfileEvent()
    object LogoutClicked : ProfileEvent()
    object BackClicked : ProfileEvent()
    object ErrorDismissed : ProfileEvent()
    data class AvatarPicked(val uri: Uri) : ProfileEvent()
    object ThemeClicked : ProfileEvent()
    object LanguageClicked : ProfileEvent()
    data class ThemeSelected(val theme: AppTheme) : ProfileEvent()
    data class LanguageSelected(val language: AppLanguage) : ProfileEvent()
    object ThemeDialogDismissed : ProfileEvent()
    object LanguageDialogDismissed : ProfileEvent()
}