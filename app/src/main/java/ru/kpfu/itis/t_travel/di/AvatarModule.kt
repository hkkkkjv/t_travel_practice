package ru.kpfu.itis.t_travel.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.kpfu.itis.t_travel.presentation.common.AvatarManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AvatarModule {
    @Provides
    @Singleton
    fun provideAvatarManager(
        @ApplicationContext context: Context
    ): AvatarManager = AvatarManager(context)
}