package ru.kpfu.itis.t_travel.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.kpfu.itis.t_travel.data.local.database.dao.BudgetDao
import ru.kpfu.itis.t_travel.data.local.database.dao.ExpenseDao
import ru.kpfu.itis.t_travel.data.local.database.dao.ParticipantDao
import ru.kpfu.itis.t_travel.data.local.database.dao.SettlementDao
import ru.kpfu.itis.t_travel.data.local.database.dao.TripDao
import ru.kpfu.itis.t_travel.data.local.database.entity.BudgetCategoryEntity
import ru.kpfu.itis.t_travel.data.local.database.entity.BudgetEntity
import ru.kpfu.itis.t_travel.data.local.database.entity.ExpenseBeneficiaryEntity
import ru.kpfu.itis.t_travel.data.local.database.entity.ExpenseEntity
import ru.kpfu.itis.t_travel.data.local.database.entity.ParticipantEntity
import ru.kpfu.itis.t_travel.data.local.database.entity.SettlementEntity
import ru.kpfu.itis.t_travel.data.local.database.entity.TripEntity

@Database(
    entities = [
        TripEntity::class,
        ParticipantEntity::class,
        BudgetEntity::class,
        BudgetCategoryEntity::class,
        ExpenseEntity::class,
        ExpenseBeneficiaryEntity::class,
        SettlementEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun participantDao(): ParticipantDao
    abstract fun budgetDao(): BudgetDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun settlementDao(): SettlementDao
} 