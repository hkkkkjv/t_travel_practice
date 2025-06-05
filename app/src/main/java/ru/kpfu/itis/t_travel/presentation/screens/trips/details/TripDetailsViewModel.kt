package ru.kpfu.itis.t_travel.presentation.screens.trips.details

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripBudgetUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripDetailsUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripExpensesUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripParticipantsUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripSettlementsUseCase
import ru.kpfu.itis.t_travel.presentation.common.BaseViewModel
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import ru.kpfu.itis.t_travel.presentation.screens.trips.budget.BudgetCategoryType
import ru.kpfu.itis.t_travel.presentation.screens.trips.budget.BudgetCategoryUi
import ru.kpfu.itis.t_travel.utils.runSuspendCatching
import javax.inject.Inject

@HiltViewModel
class TripDetailsViewModel @Inject constructor(
    private val getTripDetailsUseCase: GetTripDetailsUseCase,
    private val getTripParticipantsUseCase: GetTripParticipantsUseCase,
    private val getTripExpensesUseCase: GetTripExpensesUseCase,
    private val getTripBudgetUseCase: GetTripBudgetUseCase,
    private val getTripSettlementsUseCase: GetTripSettlementsUseCase,
) : BaseViewModel() {
    private val _state = MutableStateFlow(TripDetailsState())
    val state: StateFlow<TripDetailsState> = _state.asStateFlow()

    fun onEvent(event: TripDetailsEvent) {
        when (event) {
            is TripDetailsEvent.Load -> loadTrip(event.tripId)
            is TripDetailsEvent.ErrorShown -> _state.update { it.copy(error = null) }
            TripDetailsEvent.ParticipantsClicked -> _state.update { it.copy(showParticipantsSheet = true) }
            TripDetailsEvent.ExpensesClicked -> navigate(
                NavigationAction.NavigateToAllExpenses(
                    tripId = _state.value.trip?.id.toString()
                )
            )

            TripDetailsEvent.MyDebtsClicked -> _state.update { it.copy(showMyDebtsSheet = true) }
            TripDetailsEvent.OweMeClicked -> _state.update { it.copy(showOweMeSheet = true) }
            TripDetailsEvent.AddExpenseClicked -> {
                navigate(
                    NavigationAction.NavigateToAddExpense(
                        tripId = _state.value.trip?.id.toString()
                    )
                )
            }

            TripDetailsEvent.BackClicked -> navigate(NavigationAction.NavigateBack)
        }
    }

    private fun loadTrip(tripId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val trip = runSuspendCatching { getTripDetailsUseCase(tripId) }
                .getOrElse { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                    return@launch
                }
            val participants =
                runSuspendCatching { getTripParticipantsUseCase(tripId, false) }
                    .getOrElse { error ->
                        _state.update { it.copy(isLoading = false, error = error.message) }
                        return@launch
                    }
            val budget = runSuspendCatching { getTripBudgetUseCase(tripId) }
                .getOrElse { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                    return@launch
                }
            val expenses = runSuspendCatching { getTripExpensesUseCase(tripId) }
                .getOrElse { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                    return@launch
                }
            val settlements = runSuspendCatching { getTripSettlementsUseCase(tripId) }
                .getOrNull()
            // Простейшая фильтрация долгов (пример)
            val myDebts = expenses.filter { /* TODO: фильтрация "мои долги" */ false }
            val oweMe = expenses.filter { /* TODO: фильтрация "мне должны" */ false }
            val categoriesUi = budget.categories.mapNotNull { cat ->
                BudgetCategoryType.fromServerName(cat.category)?.let { type ->
                    BudgetCategoryUi(type, cat.allocatedAmount)
                }
            }.toImmutableList()
            _state.update {
                it.copy(
                    trip = trip,
                    isLoading = false,
                    totalBudget = budget.totalBudget,
                    categories = categoriesUi,
                    expenses = expenses.toImmutableList(),
                    participants = participants.toImmutableList(),
                    myDebts = myDebts.toImmutableList(),
                    oweMe = oweMe.toImmutableList()
                )
            }
        }
    }

}