package ru.kpfu.itis.t_travel.domain.repository

import ru.kpfu.itis.t_travel.domain.model.Expense

interface ExpenseRepository {
    suspend fun getTripExpenses(tripId: Int, forceRefresh: Boolean = false): List<Expense>
    suspend fun getMyExpenses(tripId: Int): List<Expense>
    suspend fun addExpense(tripId: Int, expense: Expense): Expense
    suspend fun deleteExpense(tripId: Int, expenseId: Int)
}