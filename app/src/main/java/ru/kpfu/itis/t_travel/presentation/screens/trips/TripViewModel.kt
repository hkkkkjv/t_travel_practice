package ru.kpfu.itis.t_travel.presentation.screens.trips

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
open class TripViewModel @Inject constructor(
    private val repository: TripRepository,
    @ApplicationContext private val context: Context
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
            }catch (e: CancellationException) {
                throw e
            }  catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
                _events.emit(TripEvent.ShowError(e.message ?: context.getString(R.string.error_loading_trips)))
            }
        }
    }
    fun onEvent(event: TripEvent) {
        viewModelScope.launch {
            when (event) {
                is TripEvent.NavigateToTripDetail -> _events.emit(event)
                is TripEvent.ShowError -> _events.emit(event)
            }
        }
    }
}