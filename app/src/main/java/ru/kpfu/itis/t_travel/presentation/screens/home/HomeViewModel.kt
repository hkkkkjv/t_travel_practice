package ru.kpfu.itis.t_travel.presentation.screens.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripDetailsUseCase
import ru.kpfu.itis.t_travel.presentation.common.BaseViewModel
import ru.kpfu.itis.t_travel.presentation.common.FavoriteTripManager
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTripDetailsUseCase: GetTripDetailsUseCase,
    private val favoriteTripManager: FavoriteTripManager
) : BaseViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _homeState.asStateFlow()

    init {
        loadFavoriteTrip()
    }

    private fun loadFavoriteTrip() {
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
                val result =
                    getTripDetailsUseCase(favoriteTripId)
                result.onSuccess { trip ->
                    _homeState.update {
                        it.copy(
                            favoriteTrip = trip,
                            // TODO: Рассчитать totalOperationsAmount, myExpensesAmount и т.д. на основе favoriteTrip
                            isLoading = false,
                            showSetupSteps = false
                        )
                    }
                }.onFailure { error ->
                    _homeState.update { it.copy(isLoading = false, error = error.message) }
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
            HomeEvent.Refresh -> loadFavoriteTrip()
            HomeEvent.LoadTripDetails -> loadFavoriteTrip()
            HomeEvent.AllOperationsClicked -> state.value.favoriteTripId?.let { tripId ->
                navigate(
                    NavigationAction.NavigateToExpensesTab(tripId.toString())
                )
            }

            HomeEvent.ParticipantsListClicked -> state.value.favoriteTripId?.let { tripId ->
                navigate(
                    NavigationAction.NavigateToParticipants(
                        tripId.toString()
                    )
                )
            }

            HomeEvent.BudgetClicked -> state.value.favoriteTripId?.let { tripId ->
                navigate(
                    NavigationAction.NavigateToBudgetDistribution(tripId.toString())
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