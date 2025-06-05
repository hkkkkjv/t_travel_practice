package ru.kpfu.itis.t_travel.domain.model

data class Budget(
    val totalBudget: Double,
    val categories: List<BudgetCategory>
) {
    companion object {
        fun mock(totalBudget: Double): Budget {
            return Budget(
                totalBudget = totalBudget,
                categories = listOf(BudgetCategory.mock(1), BudgetCategory.mock(2))
            )
        }
    }
}
