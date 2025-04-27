package ru.kpfu.itis.t_travel.presentation.screens.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import javax.inject.Inject

@HiltViewModel
open class TripViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TripState())
    open val state: StateFlow<TripState> = _state

    private val _events = MutableSharedFlow<TripEvent>()
    open val events: SharedFlow<TripEvent> = _events

    init {
        loadTrips()
    }

    fun handleAction(action: TripAction) {
        when (action) {
            is TripAction.Refresh -> loadTrips()
            is TripAction.SelectTrip -> TODO()
        }
    }

    private fun loadTrips() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val trips = repository.getTrips()
                _state.update { it.copy(trips = trips, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
                _events.emit(TripEvent.ShowError(e.message ?: "Error loading trips"))
            }
        }
    }
}