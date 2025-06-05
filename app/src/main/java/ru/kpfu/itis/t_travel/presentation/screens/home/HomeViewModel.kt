package ru.kpfu.itis.t_travel.presentation.screens.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.t_travel.domain.useCase.auth.GetCurrentUserIdUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripBudgetUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripDetailsUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripExpensesUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripParticipantsUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripSettlementsUseCase
import ru.kpfu.itis.t_travel.presentation.common.BaseViewModel
import ru.kpfu.itis.t_travel.presentation.common.FavoriteTripManager
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import ru.kpfu.itis.t_travel.utils.runSuspendCatching
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTripDetailsUseCase: GetTripDetailsUseCase,
    private val getTripParticipantsUseCase: GetTripParticipantsUseCase,
    private val getTripExpensesUseCase: GetTripExpensesUseCase,
    private val getTripSettlementsUseCase: GetTripSettlementsUseCase,
    private val getTripBudgetUseCase: GetTripBudgetUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val favoriteTripManager: FavoriteTripManager
) : BaseViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _homeState.asStateFlow()
    private var currentUserId: Int = -1

    init {
        viewModelScope.launch {
            currentUserId = runSuspendCatching { getCurrentUserIdUseCase() }.getOrElse { error ->
                _homeState.update { it.copy(error = error.message) }
                return@launch
            }
        }
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
                            isLoading = false,
                            showSetupSteps = false
                        )
                    }
                }.onFailure { error ->
                    _homeState.update { it.copy(isLoading = false, error = error.message) }
                }
                runSuspendCatching {
                    getTripParticipantsUseCase(
                        favoriteTripId,
                        forceRefresh = forceRefresh
                    )
                }.onSuccess { participants ->
                    _homeState.update { it.copy(participants = participants.toImmutableList()) }
                }.onFailure { error ->
                    _homeState.update { it.copy(error = error.message) }
                }
                runSuspendCatching {
                    getTripParticipantsUseCase(
                        favoriteTripId,
                        forceRefresh = forceRefresh
                    )
                }.onSuccess { participants ->
                    _homeState.update { it.copy(participants = participants.toImmutableList()) }
                }.onFailure { error ->
                    _homeState.update { it.copy(error = error.message) }
                    return@onFailure
                }
                runSuspendCatching {
                    getTripBudgetUseCase(favoriteTripId)
                }.onSuccess { budget ->
                    _homeState.update { it.copy(budget = budget) }
                }.onFailure { error ->
                    _homeState.update { it.copy(error = error.message) }
                    return@onFailure
                }
                runSuspendCatching {
                    getTripExpensesUseCase(favoriteTripId)
                }.onSuccess { expenses ->
                    val totalAmount = expenses.sumOf { it.amount }
                    val myExpensesAmount =
                        expenses.filter { it.paidBy == currentUserId }.sumOf { it.amount }
                    _homeState.update {
                        it.copy(
                            totalOperationsAmount = totalAmount,
                            myExpensesAmount = myExpensesAmount
                        )
                    }
                }.onFailure { error ->
                    _homeState.update { it.copy(error = error.message) }
                    return@onFailure
                }
                runSuspendCatching {
                    getTripSettlementsUseCase(favoriteTripId)
                }.onSuccess { settlements ->
                    val myDebts =
                        settlements.settlements.filter { it.to == currentUserId }
                            .sumOf { it.amount }
                    val owedToMe =
                        settlements.settlements.filter { it.from == currentUserId }
                            .sumOf { it.amount }
                    val owedToMeParticipants = settlements.settlements
                        .asSequence()
                        .filter { it.from == currentUserId }
                        .map { it.to }
                        .distinct()
                        .map { participantId ->
                            state.value.participants.find { it.id == participantId }
                        }
                        .filterNotNull()
                        .toList()

                    _homeState.update {
                        it.copy(
                            myDebtsAmount = myDebts,
                            owedToMeAmount = owedToMe,
                            owedToMeParticipants = owedToMeParticipants.toImmutableList(),
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

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.Refresh -> loadFavoriteTrip(forceRefresh = true)
            HomeEvent.LoadTripDetails -> loadFavoriteTrip()
            HomeEvent.AllOperationsClicked -> state.value.favoriteTripId?.let { tripId ->
                navigate(
                    NavigationAction.NavigateToExpensesTab(tripId.toString())
                )
            }

            HomeEvent.ParticipantsListClicked -> {/*открытие bottom sheet*/
            }

            HomeEvent.BudgetClicked -> state.value.favoriteTripId?.let { tripId ->
                navigate(
                    NavigationAction.NavigateToTripDetails(tripId.toString())
                )
            }

            HomeEvent.MyExpensesClicked -> state.value.favoriteTripId?.let { tripId ->
                navigate(
                    NavigationAction.NavigateToExpensesTab(
                        tripId.toString(),
                        filterByCurrentUser = true
                    )
                )
            }

            HomeEvent.MyDebtsClicked -> state.value.favoriteTripId?.let { tripId ->
                navigate(
                    NavigationAction.NavigateToMyDebts(tripId.toString())
                )
            }

            HomeEvent.OwedToMeClicked -> state.value.favoriteTripId?.let { tripId ->
                navigate(
                    NavigationAction.NavigateToMyDebts(
                        tripId.toString(),
                        filterByOwedToMe = true
                    )
                )
            }

            HomeEvent.AddTripClicked -> navigate(NavigationAction.NavigateToTripCreate)
            HomeEvent.SetupProfileClicked -> navigate(NavigationAction.NavigateToProfile)
            HomeEvent.CreateTripClicked -> navigate(NavigationAction.NavigateToTripCreate)
            HomeEvent.ReadyToGoClicked -> {}
        }
    }
}