package ru.kpfu.itis.t_travel.presentation.screens.more.profile

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.domain.useCase.auth.LogoutUseCase
import ru.kpfu.itis.t_travel.domain.useCase.profile.GetAvatarUriUseCase
import ru.kpfu.itis.t_travel.domain.useCase.profile.GetProfileUseCase
import ru.kpfu.itis.t_travel.domain.useCase.profile.SaveAvatarUriUseCase
import ru.kpfu.itis.t_travel.domain.useCase.profile.UpdateProfileUseCase
import ru.kpfu.itis.t_travel.domain.useCase.profile.settings.GetLanguageUseCase
import ru.kpfu.itis.t_travel.domain.useCase.profile.settings.GetThemeUseCase
import ru.kpfu.itis.t_travel.domain.useCase.profile.settings.SetLanguageUseCase
import ru.kpfu.itis.t_travel.domain.useCase.profile.settings.SetThemeUseCase
import ru.kpfu.itis.t_travel.presentation.common.BaseViewModel
import ru.kpfu.itis.t_travel.presentation.common.settings.AppLanguage
import ru.kpfu.itis.t_travel.presentation.common.settings.AppSettingsManager
import ru.kpfu.itis.t_travel.presentation.common.settings.AppTheme
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import ru.kpfu.itis.t_travel.utils.Constants
import ru.kpfu.itis.t_travel.utils.runSuspendCatching
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getProfileUseCase: GetProfileUseCase,
    private val saveAvatarUriUseCase: SaveAvatarUriUseCase,
    private val getAvatarUriUseCase: GetAvatarUriUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val appSettingsManager: AppSettingsManager,
    private val updateProfileUseCase: UpdateProfileUseCase
) : BaseViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadProfile()
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.EditProfileClicked -> navigate(NavigationAction.NavigateToEditProfile)
            is ProfileEvent.LogoutClicked -> {
                viewModelScope.launch {
                    runSuspendCatching {
                        logoutUseCase()
                    }.onSuccess {
                        _state.update { it.copy(avatarUri = null) }
                        navigate(NavigationAction.NavigateToLogin)
                    }.onFailure { error ->
                        _state.update { it.copy(error = error.message) }
                    }
                }
            }

            is ProfileEvent.BackClicked -> {
                Log.i("ProfileEvent", "back")
                navigate(NavigationAction.NavigateBack)
            }

            is ProfileEvent.ErrorDismissed -> _state.update { it.copy(error = null) }
            is ProfileEvent.AvatarPicked -> {
                saveAvatarUriUseCase(event.uri.toString())
                _state.update { it.copy(avatarUri = event.uri) }
            }

            is ProfileEvent.ThemeClicked -> _state.update { it.copy(showThemeDialog = true) }
            is ProfileEvent.LanguageClicked -> _state.update { it.copy(showLanguageDialog = true) }
            is ProfileEvent.ThemeSelected -> {
                setThemeUseCase(event.theme.name)
                _state.update { it.copy(theme = event.theme) }
                appSettingsManager.initializeAppSettings()
            }

            is ProfileEvent.LanguageSelected -> {
                setLanguageUseCase(event.language.name)
                _state.update { it.copy(language = event.language) }
                appSettingsManager.initializeAppSettings()
            }

            ProfileEvent.ThemeDialogDismissed -> _state.update { it.copy(showThemeDialog = false) }
            ProfileEvent.LanguageDialogDismissed -> _state.update { it.copy(showLanguageDialog = false) }
            is ProfileEvent.FirstNameChanged -> _state.update { it.copy(firstName = event.value) }
            is ProfileEvent.LastNameChanged -> _state.update { it.copy(lastName = event.value) }
            is ProfileEvent.PhoneChanged -> _state.update { it.copy(phone = event.value) }
            is ProfileEvent.EmailChanged -> _state.update { it.copy(email = event.value) }
            is ProfileEvent.SaveProfileClicked -> saveProfile()
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val avatarUri = getAvatarUriUseCase()?.toUri()
            val theme = AppTheme.valueOf(getThemeUseCase())
            val language = AppLanguage.valueOf(getLanguageUseCase())
            runSuspendCatching {
                getProfileUseCase()
            }.onSuccess { user ->
                _state.update {
                    it.copy(
                        user = user,
                        isLoading = false,
                        avatarUri = avatarUri,
                        firstName = user.firstName,
                        lastName = user.lastName,
                        phone = user.phone,
                        email = user.email,
                        theme = theme,
                        language = language
                    )
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        error = error.message,
                        isLoading = false,
                        avatarUri = avatarUri,
                        theme = theme,
                        language = language
                    )
                }
            }
        }
    }

    private fun saveProfile() {
        val currentState = _state.value
        val validationError = validateEditProfileInput(currentState)
        if (validationError != null) {
            _state.update { it.copy(error = validationError) }
            return
        }
        val currentUser = state.value.user ?: return
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            val updatedUser = currentUser.copy(
                firstName = state.value.firstName,
                phone = state.value.phone,
                password = "abvgde"//Хардкодим, чтобы бэк принял,
                // он не будет его менять
                // но валидация на пароль у них стоит на будущее:)
            )
            runSuspendCatching {
                updateProfileUseCase(updatedUser)
            }.onSuccess {
                navigate(NavigationAction.NavigateToProfile)
                _state.update { it.copy(isSaving = false, user = updatedUser) }
            }.onFailure { error ->
                _state.update { it.copy(isSaving = false, error = error.message) }
            }
        }
    }

    private fun validateEditProfileInput(state: ProfileState): String? {
        return when {
            state.firstName.isBlank() -> context.getString(R.string.name_blank_error)
            !state.firstName.matches(Constants.Validation.NAME_REGEX) -> context.getString(R.string.incorrect_name)
            state.lastName.isBlank() -> context.getString(R.string.lastname_blank_error)
            !state.lastName.matches(Constants.Validation.NAME_REGEX) -> context.getString(R.string.incorrect_lastname)
            state.email.isBlank() -> context.getString(R.string.email_blank_error)
            !state.email.matches(Constants.Validation.EMAIL_REGEX) -> context.getString(R.string.incorrect_email)
            state.phone.isBlank() -> context.getString(R.string.phone_blank_error)
            !state.phone.matches(Constants.Validation.PHONE_REGEX) -> context.getString(R.string.incorrect_phone_number)
            else -> null
        }
    }
}