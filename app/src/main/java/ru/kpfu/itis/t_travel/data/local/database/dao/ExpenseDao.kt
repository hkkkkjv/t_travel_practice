package ru.kpfu.itis.t_travel.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.kpfu.itis.t_travel.data.local.database.entity.ExpenseBeneficiaryEntity
import ru.kpfu.itis.t_travel.data.local.database.entity.ExpenseEntity

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses WHERE tripId = :tripId")
    fun getExpensesForTrip(tripId: Int): List<ExpenseEntity>

    @Query("SELECT * FROM expense_beneficiaries WHERE expenseId = :expenseId")
    suspend fun getBeneficiariesForExpense(expenseId: Int): List<ExpenseBeneficiaryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenses(expenses: List<ExpenseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBeneficiaries(beneficiaries: List<ExpenseBeneficiaryEntity>)

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE tripId = :tripId")
    suspend fun deleteExpensesForTrip(tripId: Int)

    @Query("DELETE FROM expense_beneficiaries WHERE expenseId = :expenseId")
    suspend fun deleteBeneficiariesForExpense(expenseId: Int)

    @Query("SELECT * FROM expenses WHERE tripId = :tripId AND lastUpdated < :timestamp")
    suspend fun getStaleExpenses(tripId: Int, timestamp: Long): List<ExpenseEntity>

    @Query("SELECT * FROM expense_beneficiaries WHERE expenseId IN (SELECT id FROM expenses WHERE tripId = :tripId) AND lastUpdated < :timestamp")
    suspend fun getStaleBeneficiaries(tripId: Int, timestamp: Long): List<ExpenseBeneficiaryEntity>
} 