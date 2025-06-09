package ru.kpfu.itis.t_travel.presentation.screens.trips.budget

sealed class BudgetScreens {
    object Input : BudgetScreens()
    object CategorySelect : BudgetScreens()
    object Amounts : BudgetScreens()
}