package ru.kpfu.itis.t_travel.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kpfu.itis.t_travel.data.local.prefs.FavoriteTripManagerImpl
import ru.kpfu.itis.t_travel.data.local.prefs.LanguageManagerImpl
import ru.kpfu.itis.t_travel.data.local.prefs.ProfileManagerImpl
import ru.kpfu.itis.t_travel.data.local.prefs.ThemeManagerImpl
import ru.kpfu.itis.t_travel.data.local.prefs.TokenManagerImpl
import ru.kpfu.itis.t_travel.presentation.common.settings.FavoriteTripManager
import ru.kpfu.itis.t_travel.presentation.common.settings.LanguageManager
import ru.kpfu.itis.t_travel.presentation.common.settings.ProfileManager
import ru.kpfu.itis.t_travel.presentation.common.settings.ThemeManager
import ru.kpfu.itis.t_travel.presentation.common.settings.TokenManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface StorageModule {
    @Binds
    @Singleton
    fun bindTokenManager(
        tokenManagerImpl: TokenManagerImpl
    ): TokenManager

    @Binds
    @Singleton
    fun bindProfileManager(
        profileManager: ProfileManagerImpl
    ): ProfileManager

    @Binds
    @Singleton
    fun bindThemeManager(
        themeManager: ThemeManagerImpl
    ): ThemeManager

    @Binds
    @Singleton
    fun bindLanguageManager(
        languageManager: LanguageManagerImpl
    ): LanguageManager

    @Binds
    @Singleton
    fun bindFavoriteTripManager(
        favoriteTripManagerImpl: FavoriteTripManagerImpl
    ): FavoriteTripManager

}