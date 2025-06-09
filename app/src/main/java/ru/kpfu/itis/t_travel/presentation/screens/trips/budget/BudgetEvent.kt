package ru.kpfu.itis.t_travel.presentation.screens.trips.budget

sealed class BudgetEvent {
    data class TotalBudgetChanged(val value: String) : BudgetEvent()
    data class CategoryChecked(val type: BudgetCategoryType, val checked: Boolean) : BudgetEvent()
    object NextToCategorySelectClicked : BudgetEvent()
    object NextToCategoryAmountsClicked : BudgetEvent()
    object BackToCategorySelect : BudgetEvent()
    object BackToAddingParticipantsClicked : BudgetEvent()
    object BackToBudgetInputClicked : BudgetEvent()
    data class EditCategoryAmount(val category: BudgetCategoryUi) : BudgetEvent()
    data class AmountChanged(val value: String) : BudgetEvent()
    object AmountConfirmed : BudgetEvent()
    object AmountSheetDismissed : BudgetEvent()
    object ShowBudgetSheet : BudgetEvent()
    object BudgetSheetDismissed : BudgetEvent()
    object SaveBudgetClicked : BudgetEvent()
    data class Error(val message: String) : BudgetEvent()
    object ErrorDismissed : BudgetEvent()
}