package ru.kpfu.itis.t_travel.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kpfu.itis.t_travel.data.repository.AuthRepositoryImpl
import ru.kpfu.itis.t_travel.data.repository.ExpenseRepositoryImpl
import ru.kpfu.itis.t_travel.data.repository.ProfileRepositoryImpl
import ru.kpfu.itis.t_travel.data.repository.SettlementRepositoryImpl
import ru.kpfu.itis.t_travel.data.repository.TripRepositoryImpl
import ru.kpfu.itis.t_travel.domain.repository.AuthRepository
import ru.kpfu.itis.t_travel.domain.repository.ExpenseRepository
import ru.kpfu.itis.t_travel.domain.repository.ProfileRepository
import ru.kpfu.itis.t_travel.domain.repository.SettlementRepository
import ru.kpfu.itis.t_travel.domain.repository.TripRepository

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideTripRepository(tripRepository: TripRepositoryImpl): TripRepository

    @Binds
    fun provideSettlementRepository(settlementRepository: SettlementRepositoryImpl): SettlementRepository

    @Binds
    fun provideExpensesRepository(expenseRepository: ExpenseRepositoryImpl): ExpenseRepository

    @Binds
    fun provideProfileRepository(profileRepository: ProfileRepositoryImpl): ProfileRepository

    @Binds
    fun provideAuthRepository(
        authRepository: AuthRepositoryImpl
    ): AuthRepository
}