package ru.kpfu.itis.t_travel.presentation.screens.trips.budget

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class BudgetState(
    val currentScreen: BudgetScreens = BudgetScreens.Input,
    val totalBudget: String = "",
    val categories: ImmutableList<BudgetCategoryUi> = persistentListOf(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showBudgetSheet: Boolean = false,
    val showAmountSheet: Boolean = false,
    val editingCategory: BudgetCategoryUi? = null,
    val editingAmount: String = "",
    val showBudgetInput: Boolean = true,
    val showCategorySelect: Boolean = false,
    val showAmounts: Boolean = false,
) {
    val totalBudgetValue: Double get() = totalBudget.toDoubleOrNull() ?: 0.0
    val distributed: Double get() = categories.sumOf { it.amount }
    val canSubmit: Boolean get() = totalBudgetValue > 0 && distributed == totalBudgetValue && categories.all { it.amount > 0 }
    val availableCategories: ImmutableList<BudgetCategoryType>
        get() = BudgetCategoryType.entries.toImmutableList()
}
