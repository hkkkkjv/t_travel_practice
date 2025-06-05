package ru.kpfu.itis.t_travel.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.kpfu.itis.t_travel.data.local.database.entity.BudgetCategoryEntity
import ru.kpfu.itis.t_travel.data.local.database.entity.BudgetEntity

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets WHERE tripId = :tripId")
    fun getBudgetForTrip(tripId: Int): BudgetEntity?

    @Query("SELECT * FROM budget_categories WHERE tripId = :tripId")
    fun getBudgetCategoriesForTrip(tripId: Int): List<BudgetCategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgetCategories(categories: List<BudgetCategoryEntity>)

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Update
    suspend fun updateBudgetCategory(category: BudgetCategoryEntity)

    @Delete
    suspend fun deleteBudget(budget: BudgetEntity)

    @Query("DELETE FROM budget_categories WHERE tripId = :tripId")
    suspend fun deleteBudgetCategoriesForTrip(tripId: Int)

    @Query("SELECT * FROM budgets WHERE tripId = :tripId AND lastUpdated < :timestamp")
    suspend fun getStaleBudget(tripId: Int, timestamp: Long): BudgetEntity?

    @Query("SELECT * FROM budget_categories WHERE tripId = :tripId AND lastUpdated < :timestamp")
    suspend fun getStaleBudgetCategories(tripId: Int, timestamp: Long): List<BudgetCategoryEntity>
} 