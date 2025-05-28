package ru.kpfu.itis.t_travel.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripDetailsUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripDetailsUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindGetTripDetailsUseCase(
        getTripDetailsUseCaseImpl: GetTripDetailsUseCaseImpl
    ): GetTripDetailsUseCase
}