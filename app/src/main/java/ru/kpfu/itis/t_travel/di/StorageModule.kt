package ru.kpfu.itis.t_travel.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kpfu.itis.t_travel.presentation.common.FavoriteTripManager
import ru.kpfu.itis.t_travel.data.local.prefs.FavoriteTripManagerImpl
import ru.kpfu.itis.t_travel.presentation.common.TokenManager
import ru.kpfu.itis.t_travel.data.local.prefs.TokenManagerImpl
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
    fun bindFavoriteTripManager(
        favoriteTripManagerImpl: FavoriteTripManagerImpl
    ): FavoriteTripManager
}