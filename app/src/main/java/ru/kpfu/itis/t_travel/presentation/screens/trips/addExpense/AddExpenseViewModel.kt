package ru.kpfu.itis.t_travel.presentation.screens.trips.addExpense


import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.useCase.auth.GetCurrentUserIdUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.AddExpenseUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripBudgetUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripParticipantsUseCase
import ru.kpfu.itis.t_travel.presentation.common.BaseViewModel
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import ru.kpfu.itis.t_travel.presentation.screens.trips.budget.BudgetCategoryType
import ru.kpfu.itis.t_travel.presentation.screens.trips.budget.BudgetCategoryUi
import ru.kpfu.itis.t_travel.utils.runSuspendCatching
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val addExpenseUseCase: AddExpenseUseCase,
    private val getTripBudgetUseCase: GetTripBudgetUseCase,
    private val getTripParticipantsUseCase: GetTripParticipantsUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    @ApplicationContext private val context: Context
) : BaseViewModel() {

    private val _state = MutableStateFlow(AddExpenseState())
    val state: StateFlow<AddExpenseState> = _state.asStateFlow()


    fun onEvent(event: AddExpenseEvent) {
        when (event) {
            is AddExpenseEvent.Load -> loadTripDetails(event.tripId)

            is AddExpenseEvent.TitleChanged -> {
                _state.update { it.copy(title = event.value) }
            }

            is AddExpenseEvent.AmountChanged -> {
                _state.update { it.copy(amount = event.value) }
            }

            is AddExpenseEvent.CategoryChanged -> {
                _state.update { it.copy(selectedCategory = event.value) }
            }

            is AddExpenseEvent.ParticipantToggled -> {
                _state.update { currentState ->
                    val updatedParticipants = currentState.selectedParticipants.toMutableList()
                    if (event.isSelected) {
                        updatedParticipants.add(event.participant)
                    } else {
                        updatedParticipants.remove(event.participant)
                    }
                    currentState.copy(selectedParticipants = updatedParticipants.toPersistentList())
                }
            }

            is AddExpenseEvent.SubmitClicked -> {
                submitExpense()
            }

            is AddExpenseEvent.BackClicked -> {
                navigate(NavigationAction.NavigateBack)
            }

            is AddExpenseEvent.ErrorDismissed -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    private fun loadTripDetails(tripId: Int) {
        Log.i("AddExpenseViewModel", tripId.toString())
        viewModelScope.launch {
            _state.update { it.copy(tripId = tripId) }
            val categories = runSuspendCatching {
                getTripBudgetUseCase(tripId).categories
            }.getOrElse { error ->
                _state.update { it.copy(error = error.message, isLoading = false) }
                return@launch
            }
            val participants = runSuspendCatching {
                getTripParticipantsUseCase(tripId, false)
            }.getOrElse { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
                return@launch
            }.filter { it.confirmed }
            val userId = runSuspendCatching {
                getCurrentUserIdUseCase()
            }.getOrElse { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
                return@launch
            }//пока не исправили контракт на использование жвт токена используем данный useCase
            Log.i("AddExpenseViewModel", categories.toString())
            val categoriesUi = categories.mapNotNull { cat ->
                BudgetCategoryType.fromServerName(cat.category)?.let { type ->
                    BudgetCategoryUi(type, cat.allocatedAmount)
                }
            }.toImmutableList()
            Log.i("AddExpenseViewModel", categoriesUi.toString())
            _state.update {
                it.copy(
                    categories = categoriesUi,
                    participants = participants.toImmutableList(),
                    userId = userId,
                    isLoading = false
                )
            }
        }
    }

    private fun submitExpense() {
        val currentState = _state.value
        if (!validateInput(currentState)) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            runSuspendCatching {
                addExpenseUseCase(
                    tripId = currentState.tripId,
                    expense = Expense(
                        tripId = currentState.tripId,
                        description = currentState.title,
                        amount = currentState.amount.toDoubleOrNull() ?: 0.0,
                        //category = currentState.selectedCategory?.id ?: 0,
                        beneficiaries = currentState.selectedParticipants.map { it.id },
                        paidBy = state.value.userId
                    ),
                )
            }.onSuccess {
                navigate(NavigationAction.NavigateBack)
            }.onFailure { error ->
                _state.update { it.copy(error = error.message) }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun validateInput(state: AddExpenseState): Boolean {
        if (state.title.isBlank()) {
            _state.update { it.copy(error = context.getString(R.string.error_enter_expense_title)) }
            return false
        }
        if (state.amount.isBlank() || state.amount.toDoubleOrNull() == null) {
            _state.update { it.copy(error = context.getString(R.string.error_enter_valid_amount)) }
            return false
        }
        if (state.selectedParticipants.isEmpty()) {
            _state.update { it.copy(error = context.getString(R.string.error_select_participant)) }
            return false
        }
        return true
    }
}