package ru.kpfu.itis.t_travel.presentation.screens.more.profile

import android.net.Uri
import ru.kpfu.itis.t_travel.presentation.common.settings.AppLanguage
import ru.kpfu.itis.t_travel.presentation.common.settings.AppTheme

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
    data class FirstNameChanged(val value: String) : ProfileEvent()
    data class LastNameChanged(val value: String) : ProfileEvent()
    data class PhoneChanged(val value: String) : ProfileEvent()
    data class EmailChanged(val value: String) : ProfileEvent()
    object SaveProfileClicked : ProfileEvent()
}