package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName
import ru.kpfu.itis.t_travel.domain.model.Budget

data class BudgetDto(
    @SerializedName("totalBudget") val totalBudget: Double,
    @SerializedName("categories") val categories: List<BudgetCategoryDto>
) {
    fun toDomain(tripId: Int): Budget {
        return Budget(
            tripId = tripId,
            totalBudget = totalBudget,
            categories = categories.map { it.toDomain() }
        )
    }
}
