package ru.kpfu.itis.t_travel.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kpfu.itis.t_travel.data.remote.ApiService
import ru.kpfu.itis.t_travel.data.repository.AuthRepositoryImpl
import ru.kpfu.itis.t_travel.data.repository.TripRepositoryImpl
import ru.kpfu.itis.t_travel.domain.repository.AuthRepository
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import ru.kpfu.itis.t_travel.presentation.common.TokenManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTripRepository(apiService: ApiService): TripRepository {
        return TripRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: ApiService,
        tokenManager: TokenManager
    ): AuthRepository {
        return AuthRepositoryImpl(apiService, tokenManager)
    }
}