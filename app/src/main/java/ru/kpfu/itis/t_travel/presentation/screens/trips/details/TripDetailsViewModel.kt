package ru.kpfu.itis.t_travel.presentation.screens.trips.details

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
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripDetailsUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.budget.GetTripBudgetUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.expense.GetTripExpensesUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.participant.GetTripParticipantsUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.settlement.ConfirmDebtReturnUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.settlement.GetPayableSettlementsUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.settlement.GetReceivableSettlementsUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.settlement.RequestDebtConfirmationUseCase
import ru.kpfu.itis.t_travel.presentation.common.BaseViewModel
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import ru.kpfu.itis.t_travel.presentation.screens.trips.budget.BudgetCategoryType
import ru.kpfu.itis.t_travel.presentation.screens.trips.budget.BudgetCategoryUi
import ru.kpfu.itis.t_travel.utils.runSuspendCatching
import javax.inject.Inject

@HiltViewModel
class TripDetailsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getTripDetailsUseCase: GetTripDetailsUseCase,
    private val getTripParticipantsUseCase: GetTripParticipantsUseCase,
    private val getTripExpensesUseCase: GetTripExpensesUseCase,
    private val getTripBudgetUseCase: GetTripBudgetUseCase,
    private val getPayableSettlementsUseCase: GetPayableSettlementsUseCase,
    private val getReceivableSettlementsUseCase: GetReceivableSettlementsUseCase,
    private val requestDebtConfirmationUseCase: RequestDebtConfirmationUseCase,
    private val confirmDebtReturnUseCase: ConfirmDebtReturnUseCase,
) : BaseViewModel() {
    private val _state = MutableStateFlow(TripDetailsState())
    val state: StateFlow<TripDetailsState> = _state.asStateFlow()

    fun onEvent(event: TripDetailsEvent) {
        when (event) {
            TripDetailsEvent.Refresh -> state.value.trip?.id?.let { loadTrip(it, true) }
            is TripDetailsEvent.Load -> loadTrip(event.tripId)
            is TripDetailsEvent.ErrorShown -> _state.update { it.copy(error = null) }
            TripDetailsEvent.ParticipantsClicked -> _state.update { it.copy(showParticipantsSheet = true) }
            TripDetailsEvent.ExpensesClicked -> _state.update { it.copy(showExpensesSheet = true) }

            TripDetailsEvent.MyDebtsClicked -> _state.update { it.copy(showMyDebtsSheet = true) }
            TripDetailsEvent.OweMeClicked -> _state.update { it.copy(showOwedMeSheet = true) }
            TripDetailsEvent.AddExpenseClicked -> {
                navigate(
                    NavigationAction.NavigateToAddExpense(
                        tripId = _state.value.trip?.id.toString()
                    )
                )
            }

            TripDetailsEvent.BackClicked -> navigate(NavigationAction.NavigateBack)
            TripDetailsEvent.DismissParticipantsSheet -> _state.update {
                it.copy(
                    showParticipantsSheet = false
                )
            }

            TripDetailsEvent.DismissExpensesSheet -> _state.update {
                it.copy(
                    showExpensesSheet = false
                )
            }

            TripDetailsEvent.DismissMyDebtsSheet -> _state.update { it.copy(showMyDebtsSheet = false) }
            TripDetailsEvent.DismissOwedToMeSheet -> _state.update { it.copy(showOwedMeSheet = false) }
            is TripDetailsEvent.AddParticipantsClicked -> {
                navigate(
                    NavigationAction.NavigateToAddParticipants(
                        event.tripId.toString(),
                        fromBottomSheet = true
                    )
                )
            }

            is TripDetailsEvent.ConfirmDebtReturn -> viewModelScope.launch {
                val tripId = state.value.trip?.id ?: return@launch
                runSuspendCatching {
                    confirmDebtReturnUseCase(tripId, event.settlement.id)
                }
                loadTrip(tripId, forceRefresh = true)
            }

            is TripDetailsEvent.RequestDebtConfirmation -> viewModelScope.launch {
                val tripId = state.value.trip?.id ?: return@launch
                runSuspendCatching {
                    requestDebtConfirmationUseCase(tripId, event.settlement.id)
                }
                loadTrip(tripId, forceRefresh = true)
            }
        }
    }

    private fun loadTrip(tripId: Int, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val trip =
                runSuspendCatching { getTripDetailsUseCase(tripId, forceRefresh = forceRefresh) }
                    .getOrElse { e ->
                        _state.update { it.copy(isLoading = false, error = e.message) }
                        return@launch
                    }
            val participants =
                runSuspendCatching {
                    getTripParticipantsUseCase(
                        tripId,
                        forceRefresh = forceRefresh
                    )
                }
                    .getOrElse { error ->
                        _state.update { it.copy(isLoading = false, error = error.message) }
                        return@launch
                    }
            val budget =
                runSuspendCatching { getTripBudgetUseCase(tripId, forceRefresh = forceRefresh) }
                    .getOrElse { error ->
                        _state.update {
                            it.copy(
                                isLoading = false, error = error.message + context.getString(
                                    R.string.the_trip_don_t_have_budget_so_you_have_not_trip
                                )
                            )
                        }
                        return@launch
                    }
            val expenses =
                runSuspendCatching { getTripExpensesUseCase(tripId, forceRefresh = forceRefresh) }
                    .getOrElse { error ->
                        _state.update { it.copy(isLoading = false, error = error.message) }
                        return@launch
                    }
            val payableSettlements = runSuspendCatching {
                getPayableSettlementsUseCase(tripId)
            }.getOrElse { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
                return@launch
            }
            val receivableSettlements = runSuspendCatching {
                getReceivableSettlementsUseCase(tripId)
            }.getOrElse { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
                return@launch
            }
            val categoriesUi = budget.categories.mapNotNull { cat ->
                BudgetCategoryType.fromServerName(cat.category)?.let { type ->
                    BudgetCategoryUi(type = type, amount = cat.allocatedAmount)
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
                    myDebts = payableSettlements.toImmutableList(),
                    oweMe = receivableSettlements.toImmutableList(),
                )
            }
        }
    }

}