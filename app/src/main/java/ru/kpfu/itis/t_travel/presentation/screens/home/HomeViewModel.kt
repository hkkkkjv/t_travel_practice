package ru.kpfu.itis.t_travel.presentation.screens.home

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
import ru.kpfu.itis.t_travel.domain.model.SettlementItem
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripDetailsUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.budget.GetTripBudgetUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.expense.GetMyExpensesUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.expense.GetTripExpensesUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.participant.GetTripParticipantsUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.settlement.ConfirmDebtReturnUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.settlement.GetPayableSettlementsUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.settlement.GetReceivableSettlementsUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.settlement.RequestDebtConfirmationUseCase
import ru.kpfu.itis.t_travel.presentation.common.BaseViewModel
import ru.kpfu.itis.t_travel.presentation.common.settings.FavoriteTripManager
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import ru.kpfu.itis.t_travel.utils.runSuspendCatching
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getTripDetailsUseCase: GetTripDetailsUseCase,
    private val getTripParticipantsUseCase: GetTripParticipantsUseCase,
    private val getTripExpensesUseCase: GetTripExpensesUseCase,
    private val getMyExpensesUseCase: GetMyExpensesUseCase,
    private val getTripBudgetUseCase: GetTripBudgetUseCase,
    private val getPayableSettlementsUseCase: GetPayableSettlementsUseCase,
    private val getReceivableSettlementsUseCase: GetReceivableSettlementsUseCase,
    private val requestDebtConfirmationUseCase: RequestDebtConfirmationUseCase,
    private val confirmDebtReturnUseCase: ConfirmDebtReturnUseCase,
    private val favoriteTripManager: FavoriteTripManager
) : BaseViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _homeState.asStateFlow()

    init {
        loadFavoriteTrip()
    }

    private fun loadFavoriteTrip(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            val favoriteTripId = favoriteTripManager.getFavoriteTripId()

            if (favoriteTripId != null) {
                _homeState.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                        showSetupSteps = false,
                        favoriteTripId = favoriteTripId
                    )
                }
                runSuspendCatching {
                    getTripDetailsUseCase(favoriteTripId)
                }.onSuccess { trip ->
                    _homeState.update {
                        it.copy(
                            favoriteTrip = trip,
                            showSetupSteps = false
                        )
                    }
                }.onFailure { error ->
                    _homeState.update { it.copy(isLoading = false, error = error.message) }
                }
                runSuspendCatching {
                    getTripParticipantsUseCase(favoriteTripId, forceRefresh)
                }.onSuccess { participants ->
                    _homeState.update { it.copy(participants = participants.toImmutableList()) }
                }.onFailure { error ->
                    _homeState.update { it.copy(error = error.message) }
                }
                runSuspendCatching {
                    getTripBudgetUseCase(favoriteTripId)
                }.onSuccess { budget ->
                    _homeState.update { it.copy(budget = budget) }
                }.onFailure { error ->
                    _homeState.update {
                        it.copy(
                            error = error.message + context.getString(
                                R.string.the_trip_don_t_have_budget_so_you_have_not_trip
                            )
                        )
                    }
                    return@onFailure
                }
                runSuspendCatching {
                    getTripExpensesUseCase(favoriteTripId)
                }.onSuccess { expenses ->
                    val totalAmount = expenses.sumOf { it.amount }
                    _homeState.update {
                        it.copy(
                            expenses = expenses.toImmutableList(),
                            totalOperationsAmount = totalAmount,
                        )
                    }
                }.onFailure { error ->
                    _homeState.update { it.copy(error = error.message) }
                    return@onFailure
                }
                runSuspendCatching {
                    getMyExpensesUseCase(favoriteTripId)
                }.onSuccess { expenses ->
                    val myExpensesAmount = expenses.sumOf { it.amount }
                    _homeState.update {
                        it.copy(
                            myExpenses = expenses.toImmutableList(),
                            myExpensesAmount = myExpensesAmount
                        )
                    }
                }.onFailure { error ->
                    _homeState.update { it.copy(error = error.message) }
                    return@onFailure
                }
                runSuspendCatching {
                    getReceivableSettlementsUseCase(favoriteTripId)
                }.onSuccess { settlements ->
                    val sorted = sortOweMe(settlements)
                    val filtered =
                        sorted.filter { it.status == "PENDING" || it.status == "REQUESTED" }
                    val participants =
                        state.value.participants.filter { participant -> settlements.any { participant.id == it.to } }
                            .toImmutableList()
                    _homeState.update {
                        it.copy(
                            participantsOwedToMe = participants,
                            oweMe = sorted.toImmutableList(),
                            totalReceivableAmount = filtered.sumOf { it.amount }
                        )
                    }
                }.onFailure { error ->
                    _homeState.update { it.copy(error = error.message) }
                    return@onFailure
                }
                runSuspendCatching {
                    getPayableSettlementsUseCase(favoriteTripId)
                }.onSuccess { settlements ->
                    val sorted = sortMyDebts(settlements)
                    val filtered =
                        sorted.filter { it.status == "PENDING" || it.status == "REQUESTED" }
                    val participants =
                        state.value.participants.filter { participant -> settlements.any { participant.id == it.from } }
                            .toImmutableList()
                    _homeState.update {
                        it.copy(
                            totalPayableAmount = filtered.sumOf { amount -> amount.amount },
                            debtsParticipants = participants,
                            myDebts = sorted.toImmutableList(),
                            isLoading = false,
                            showSetupSteps = false
                        )
                    }
                }.onFailure { error ->
                    _homeState.update { it.copy(error = error.message) }
                    return@onFailure
                }
            } else {
                _homeState.update {
                    it.copy(
                        showSetupSteps = true,
                        isLoading = false,
                        favoriteTrip = null
                    )
                }
            }
        }
    }

    private fun sortMyDebts(debts: List<SettlementItem>): List<SettlementItem> {
        return debts.sortedWith(compareBy({ statusOrderMyDebts(it.status) }))
    }

    private fun sortOweMe(debts: List<SettlementItem>): List<SettlementItem> {
        return debts.sortedWith(compareBy({ statusOrderOweMe(it.status) }))
    }

    private fun statusOrderMyDebts(status: String): Int = when (status) {
        "PENDING" -> 0
        "REQUESTED" -> 1
        "CONFIRMED" -> 2
        else -> 3
    }

    private fun statusOrderOweMe(status: String): Int = when (status) {
        "REQUESTED" -> 0
        "PENDING" -> 1
        "CONFIRMED" -> 2
        else -> 3
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.Refresh -> loadFavoriteTrip(forceRefresh = true)
            HomeEvent.LoadTripDetails -> loadFavoriteTrip()
            HomeEvent.AllOperationsClicked -> _homeState.update { it.copy(showExpensesSheet = true) }

            HomeEvent.ParticipantsListClicked -> _homeState.update { it.copy(showParticipantsSheet = true) }

            HomeEvent.BudgetClicked -> state.value.favoriteTripId?.let { tripId ->
                navigate(
                    NavigationAction.NavigateToTripDetails(tripId.toString())
                )
            }

            HomeEvent.MyExpensesClicked -> _homeState.update { it.copy(showMyExpensesSheet = true) }
            HomeEvent.MyDebtsClicked -> _homeState.update { it.copy(showMyDebtsSheet = true) }
            HomeEvent.OwedToMeClicked -> _homeState.update { it.copy(showOweMeSheet = true) }
            HomeEvent.AddTripClicked -> navigate(NavigationAction.NavigateToTripCreate)
            HomeEvent.SetupProfileClicked -> navigate(NavigationAction.NavigateToProfile)
            HomeEvent.CreateTripClicked -> navigate(NavigationAction.NavigateToTripCreate)
            HomeEvent.ReadyToGoClicked -> {}
            HomeEvent.DismissParticipantsSheet -> _homeState.update { it.copy(showParticipantsSheet = false) }
            HomeEvent.DismissExpensesSheet -> _homeState.update { it.copy(showExpensesSheet = false) }
            HomeEvent.DismissMyExpensesSheet -> _homeState.update { it.copy(showMyExpensesSheet = false) }
            HomeEvent.ShowOweMeSheet -> _homeState.update { it.copy(showOweMeSheet = true) }
            HomeEvent.ShowMyDebtsSheet -> _homeState.update { it.copy(showMyDebtsSheet = true) }
            HomeEvent.DismissOweMeSheet -> _homeState.update { it.copy(showOweMeSheet = false) }
            HomeEvent.DismissMyDebtsSheet -> _homeState.update { it.copy(showMyDebtsSheet = false) }
            is HomeEvent.RequestDebtConfirmation -> {
                viewModelScope.launch {
                    val tripId = state.value.favoriteTripId ?: return@launch
                    runSuspendCatching {
                        requestDebtConfirmationUseCase(tripId, event.settlement.id)
                    }
                    loadFavoriteTrip(forceRefresh = true)
                }
            }

            is HomeEvent.ConfirmDebtReturn -> {
                viewModelScope.launch {
                    val tripId = state.value.favoriteTripId ?: return@launch
                    runSuspendCatching {
                        confirmDebtReturnUseCase(tripId, event.settlement.id)
                    }
                    loadFavoriteTrip(forceRefresh = true)
                }
            }

            is HomeEvent.AddParticipantsClicked -> {
                navigate(
                    NavigationAction.NavigateToAddParticipants(
                        event.tripId.toString(),
                        fromBottomSheet = true
                    )
                )
            }
        }
    }
}