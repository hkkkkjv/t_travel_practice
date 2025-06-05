package ru.kpfu.itis.t_travel.presentation.screens.more.profile

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.t_travel.domain.useCase.auth.LogoutUseCase
import ru.kpfu.itis.t_travel.domain.useCase.profile.GetAvatarUriUseCase
import ru.kpfu.itis.t_travel.domain.useCase.profile.GetProfileUseCase
import ru.kpfu.itis.t_travel.domain.useCase.profile.SaveAvatarUriUseCase
import ru.kpfu.itis.t_travel.domain.useCase.profile.settings.SetLanguageUseCase
import ru.kpfu.itis.t_travel.domain.useCase.profile.settings.SetThemeUseCase
import ru.kpfu.itis.t_travel.presentation.common.AppSettingsManager
import ru.kpfu.itis.t_travel.presentation.common.BaseViewModel
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import ru.kpfu.itis.t_travel.utils.runSuspendCatching
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val saveAvatarUriUseCase: SaveAvatarUriUseCase,
    private val getAvatarUriUseCase: GetAvatarUriUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val appSettingsManager: AppSettingsManager
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

            is ProfileEvent.BackClicked -> navigate(NavigationAction.NavigateBack)
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
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val avatarUri = getAvatarUriUseCase()?.toUri()
            runSuspendCatching {
                getProfileUseCase()
            }.onSuccess { user ->
                _state.update {
                    it.copy(
                        user = user,
                        isLoading = false,
                        avatarUri = avatarUri
                    )
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        error = error.message,
                        isLoading = false,
                        avatarUri = avatarUri
                    )
                }
            }
        }
    }
}