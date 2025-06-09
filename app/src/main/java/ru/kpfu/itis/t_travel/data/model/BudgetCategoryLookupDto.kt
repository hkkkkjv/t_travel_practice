package ru.kpfu.itis.t_travel.data.model

import com.google.gson.annotations.SerializedName
import ru.kpfu.itis.t_travel.domain.model.BudgetCategoryLookup

data class BudgetCategoryLookupDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
) {
    fun toDomain(): BudgetCategoryLookup {
        return BudgetCategoryLookup(
            id = id,
            name = name
        )
    }
}