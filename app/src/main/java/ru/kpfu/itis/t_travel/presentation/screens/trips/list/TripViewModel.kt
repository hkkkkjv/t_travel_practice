package ru.kpfu.itis.t_travel.presentation.screens.trips.list

import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.domain.useCase.auth.GetCurrentUserIdUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.ConfirmParticipationUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetAllTripsUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripExpensesUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.GetTripParticipantsUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.RejectParticipationUseCase
import ru.kpfu.itis.t_travel.presentation.common.BaseViewModel
import ru.kpfu.itis.t_travel.presentation.common.FavoriteTripManager
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import ru.kpfu.itis.t_travel.utils.runSuspendCatching
import javax.inject.Inject

@HiltViewModel
open class TripViewModel @Inject constructor(
    private val getAllTripsUseCase: GetAllTripsUseCase,
    private val getTripParticipantsUseCase: GetTripParticipantsUseCase,
    private val getTripExpensesUseCase: GetTripExpensesUseCase,
    private val confirmParticipationUseCase: ConfirmParticipationUseCase,
    private val rejectParticipationUseCase: RejectParticipationUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    @ApplicationContext private val context: Context,
    private val favoriteTripManager: FavoriteTripManager
) : BaseViewModel() {

    private val _state = MutableStateFlow(TripState())
    val state: StateFlow<TripState> = _state

    private val _events = MutableSharedFlow<TripEvent>()
    val events: SharedFlow<TripEvent> = _events
    private var currentUserId: Int = -1

    init {
        viewModelScope.launch {
            currentUserId = getCurrentUserIdUseCase()
        }
        loadTrips(forceRefresh = true)
    }

    fun handleAction(action: TripAction) {
        when (action) {
            is TripAction.Refresh -> loadTrips(forceRefresh = true)
            is TripAction.AcceptInvitation -> acceptInvitation(action.tripId)
            is TripAction.DeclineInvitation -> declineInvitation(action.tripId)
            is TripAction.SelectTrip ->
                navigate(
                    NavigationAction.NavigateToTripDetails(action.tripId.toString())
                )

            TripAction.CreateTrip -> navigate(NavigationAction.NavigateToTripCreate)
            is TripAction.SetFavorite -> setFavoriteTrip(action.tripId)
        }
    }

    private fun setFavoriteTrip(tripId: Int) {
        viewModelScope.launch {
            favoriteTripManager.saveFavoriteTripId(tripId)
            onEvent(
                TripEvent.ShowMessage(
                    context.getString(
                        R.string.trip_was_added_in_favorite,
                        tripId.toString()
                    )
                )
            )
        }
    }

    private fun declineInvitation(tripId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runSuspendCatching {
                rejectParticipationUseCase(tripId, currentUserId)
            }.onSuccess {
                loadTrips()
                onEvent(TripEvent.ShowMessage(context.getString(R.string.invitation_reject)))
                _state.update { it.copy(isLoading = false) }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        error = error.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun acceptInvitation(tripId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            runSuspendCatching {
                confirmParticipationUseCase(tripId, currentUserId)
            }.onSuccess {
                loadTrips()
                onEvent(TripEvent.ShowMessage(context.getString(R.string.invitation_confitm)))
                _state.update { it.copy(isLoading = false) }
            }.onFailure { error ->
                onEvent(
                    TripEvent.ShowError(
                        error.message ?: context.getString(R.string.invitation_confirm_error)
                    )
                )
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadTrips(forceRefresh: Boolean = true) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            runSuspendCatching {
                val trips = getAllTripsUseCase(forceRefresh = forceRefresh)
                val tripsWithDetails = trips.map { trip ->
                    async {
                        val participants = getTripParticipantsUseCase(trip.id, forceRefresh)
                        val expenses = getTripExpensesUseCase(trip.id)
                        trip to (participants to expenses)
                    }
                }.awaitAll()

                val participantsMap = tripsWithDetails.associate {
                    it.first.id to it.second.first.toImmutableList()
                }.toPersistentMap()

                val expensesMap = tripsWithDetails.associate {
                    it.first.id to it.second.second.toImmutableList()
                }.toPersistentMap()

                _state.update {
                    it.copy(
                        trips = trips.toImmutableList(),
                        participantsByTripId = participantsMap,
                        expensesByTripId = expensesMap,
                        isLoading = false
                    )
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = error.message ?: context.getString(R.string.error_loading_trips)
                    )
                }
                onEvent(
                    TripEvent.ShowError(
                        error.message ?: context.getString(R.string.error_loading_trips)
                    )
                )
            }
        }
    }

    private fun onEvent(event: TripEvent) {
        viewModelScope.launch {
            when (event) {
                is TripEvent.ShowError -> _events.emit(event)
                is TripEvent.ShowMessage -> _events.emit(event)
            }
        }
    }
}