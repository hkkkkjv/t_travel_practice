package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName
import ru.kpfu.itis.t_travel.domain.model.BudgetCategory

data class BudgetCategoryDto (
    @SerializedName("category") val category: String,
    @SerializedName("allocatedAmount") val allocatedAmount: Double
){
    fun toDomain(): BudgetCategory{
        return BudgetCategory(
            category = category,
            allocatedAmount = allocatedAmount
        )
    }
}
