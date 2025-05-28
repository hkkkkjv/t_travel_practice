package ru.kpfu.itis.t_travel.domain.model

data class BudgetCategory(
    val category: String,
    val allocatedAmount: Double
) {
    companion object {
        fun mock(id: Int): BudgetCategory {
            return BudgetCategory(
                category = "Категория${id}",
                allocatedAmount = 10000.0
            )
        }
    }
}