package ru.kpfu.itis.t_travel.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.kpfu.itis.t_travel.data.local.database.AppDatabase
import ru.kpfu.itis.t_travel.data.local.database.dao.TripDao
import ru.kpfu.itis.t_travel.utils.Constants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            Constants.Database.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideTripDao(database: AppDatabase): TripDao = database.tripDao()

    @Provides
    fun provideParticipantDao(database: AppDatabase) = database.participantDao()

    @Provides
    fun provideBudgetDao(database: AppDatabase) = database.budgetDao()

    @Provides
    fun provideExpenseDao(database: AppDatabase) = database.expenseDao()

    @Provides
    fun provideSettlementDao(database: AppDatabase) = database.settlementDao()
}