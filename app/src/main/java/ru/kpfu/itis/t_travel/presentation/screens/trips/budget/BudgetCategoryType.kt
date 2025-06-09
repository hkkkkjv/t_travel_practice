package ru.kpfu.itis.t_travel.presentation.screens.trips.budget

import ru.kpfu.itis.t_travel.R

enum class BudgetCategoryType(val nameToServer: String, val nameRes: Int, val colorRes: Int) {
    TICKETS("Билеты", R.string.category_tickets, R.color.category_tickets),
    HOTELS("Отели", R.string.category_hotels, R.color.category_hotels),
    FOOD("Питание", R.string.category_food, R.color.category_food),
    ENTERTAINMENT("Развлечения", R.string.category_entertainment, R.color.category_entertainment),
    INSURANCE("Страховка", R.string.category_insurance, R.color.category_insurance),
    TRANSPORT("Транспорт", R.string.category_transport, R.color.category_transport),
    GIFTS("Подарки", R.string.category_gifts, R.color.category_gifts),
    OTHER("Другое", R.string.category_other, R.color.category_other);
    companion object {
        fun fromServerName(name: String): BudgetCategoryType? =
            entries.find { it.nameToServer == name }
    }
}