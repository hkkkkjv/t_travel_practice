package ru.kpfu.itis.t_travel.presentation.screens.trips.budget

import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.domain.model.Budget
import ru.kpfu.itis.t_travel.domain.model.BudgetCategory
import ru.kpfu.itis.t_travel.domain.useCase.trip.budget.SetBudgetUseCase
import ru.kpfu.itis.t_travel.presentation.common.BaseViewModel
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import ru.kpfu.itis.t_travel.utils.runSuspendCatching
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val setBudgetUseCase: SetBudgetUseCase,
    @ApplicationContext private val context: Context
) : BaseViewModel() {

    private val _state = MutableStateFlow(BudgetState())
    val state: StateFlow<BudgetState> = _state.asStateFlow()
    private var tripId: Int = -1

    fun init(tripId: Int) {
        this.tripId = tripId
    }

    private var editingAmount: String = ""

    fun onEvent(event: BudgetEvent) {
        when (event) {
            is BudgetEvent.TotalBudgetChanged -> {
                _state.value = _state.value.copy(totalBudget = event.value)
            }

            is BudgetEvent.CategoryChecked -> {
                val currentCategories = _state.value.categories
                val newCategories = if (event.checked) {
                    currentCategories + BudgetCategoryUi(
                        type = event.type
                    )
                } else {
                    currentCategories.filter { it.type != event.type }
                }
                _state.value = _state.value.copy(categories = newCategories.toImmutableList())
            }

            is BudgetEvent.EditCategoryAmount -> {
                editingAmount = event.category.amount.takeIf { it > 0 }?.toString() ?: ""
                _state.value = _state.value.copy(
                    showAmountSheet = true,
                    editingCategory = event.category
                )
            }

            is BudgetEvent.AmountChanged -> {
                editingAmount = event.value
            }

            is BudgetEvent.AmountConfirmed -> {
                val amount = editingAmount.toDoubleOrNull() ?: 0.0
                val currentCategories = _state.value.categories.toMutableList()
                val index =
                    currentCategories.indexOfFirst { it.type == _state.value.editingCategory?.type }
                if (index != -1) {
                    currentCategories[index] = currentCategories[index].copy(amount = amount)
                    _state.value = _state.value.copy(
                        categories = currentCategories.toImmutableList(),
                        showAmountSheet = false,
                        editingCategory = null
                    )
                }
            }

            is BudgetEvent.AmountSheetDismissed -> {
                _state.value = _state.value.copy(
                    showAmountSheet = false,
                    editingCategory = null
                )
            }

            is BudgetEvent.SaveBudgetClicked -> saveBudget()

            is BudgetEvent.Error -> {
                _state.value = _state.value.copy(error = event.message)
            }

            is BudgetEvent.ErrorDismissed -> {
                _state.value = _state.value.copy(error = null)
            }

            BudgetEvent.NextToCategorySelectClicked -> _state.update {
                it.copy(
                    currentScreen = BudgetScreens.CategorySelect
                )
            }

            BudgetEvent.NextToCategoryAmountsClicked -> _state.update {
                it.copy(
                    currentScreen = BudgetScreens.Amounts
                )
            }

            BudgetEvent.BudgetSheetDismissed -> _state.value =
                _state.value.copy(showBudgetSheet = false)

            BudgetEvent.ShowBudgetSheet -> _state.value = _state.value.copy(showBudgetSheet = true)
            BudgetEvent.BackToBudgetInputClicked -> _state.update {
                it.copy(currentScreen = BudgetScreens.Input)
            }

            BudgetEvent.BackToCategorySelect -> _state.update {
                it.copy(currentScreen = BudgetScreens.CategorySelect)
            }

            BudgetEvent.BackToAddingParticipantsClicked -> navigate(NavigationAction.NavigateBack)
        }
    }

    private fun saveBudget() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            runSuspendCatching {
                val categories = _state.value.categories.map {
                    BudgetCategory(
                        category = it.type.nameToServer,
                        allocatedAmount = it.amount
                    )
                }
                val budget = Budget(
                    totalBudget = _state.value.totalBudgetValue,
                    categories = categories,
                    tripId = tripId
                )

                setBudgetUseCase(tripId, budget)
                navigate(NavigationAction.NavigateToTrips)
                _state.value = _state.value.copy(isLoading = false)

            }.onFailure { error ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = error.message ?: context.getString(R.string.failed_to_save_budget)
                )
            }
        }
    }

}