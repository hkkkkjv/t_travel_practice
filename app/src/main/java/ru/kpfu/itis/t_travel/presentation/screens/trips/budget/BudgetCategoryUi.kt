package ru.kpfu.itis.t_travel.presentation.screens.trips.budget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource


data class BudgetCategoryUi(
    val type: BudgetCategoryType,
    val amount: Double = 0.0
) {
    @Composable
    fun name() = stringResource(id = type.nameRes)
    @Composable
    fun color(): Color = colorResource(id = type.colorRes)
}
