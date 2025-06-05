package ru.kpfu.itis.t_travel.presentation.screens.trips.create

import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.domain.model.Trip
import ru.kpfu.itis.t_travel.domain.useCase.trip.create.CreateTripUseCase
import ru.kpfu.itis.t_travel.presentation.common.BaseViewModel
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import ru.kpfu.itis.t_travel.utils.runSuspendCatching
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class NewTripViewModel @Inject constructor(
    private val createTripUseCase: CreateTripUseCase,
    @ApplicationContext private val context: Context
) : BaseViewModel() {

    private val _state = MutableStateFlow(NewTripState())
    val state: StateFlow<NewTripState> = _state.asStateFlow()

    fun onEvent(event: NewTripEvent) {
        when (event) {
            is NewTripEvent.TitleChanged -> _state.update { it.copy(title = event.value) }
            is NewTripEvent.DescriptionChanged -> _state.update { it.copy(description = event.value) }
            is NewTripEvent.DepartureCityChanged -> _state.update { it.copy(departureCity = event.value) }
            is NewTripEvent.DestinationCityChanged -> _state.update { it.copy(destinationCity = event.value) }
            is NewTripEvent.StartDateChanged -> _state.update { it.copy(startDate = event.value) }
            is NewTripEvent.EndDateChanged -> _state.update { it.copy(endDate = event.value) }
            is NewTripEvent.ShowStartDatePicker -> _state.update { it.copy(showDatePickerStart = true) }
            is NewTripEvent.StartDateSelected -> _state.update {
                it.copy(
                    startDate = event.date,
                    showDatePickerStart = false,
                )
            }

            is NewTripEvent.EndDateSelected -> _state.update {
                it.copy(
                    endDate = event.date,
                    showDatePickerEnd = false
                )
            }

            is NewTripEvent.CreateTripClicked -> createTrip()
            is NewTripEvent.ErrorShown -> _state.update { it.copy(error = null) }
            NewTripEvent.BackClicked -> navigate(NavigationAction.NavigateBack)
            is NewTripEvent.DateRangeChanged -> _state.update {
                it.copy(
                    startDate = event.start,
                    endDate = event.end
                )
            }

            is NewTripEvent.ShowEndDatePicker -> _state.update { it.copy(showDatePickerEnd = true) }
            is NewTripEvent.HideDatePickers -> _state.update {
                it.copy(
                    showDatePickerStart = false,
                    showDatePickerEnd = false
                )
            }
        }
    }

    private fun createTrip() {
        val error = validate()
        if (error != null) {
            _state.update { it.copy(error = error) }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val trip = Trip(
                id = 0,
                title = _state.value.title,
                description = _state.value.description,
                startDate = _state.value.startDate ?: LocalDate.now(),
                endDate = _state.value.endDate ?: LocalDate.now(),
                departureCity = _state.value.departureCity,
                destinationCity = _state.value.destinationCity,
            )
            runSuspendCatching {createTripUseCase(trip)}
            .onSuccess { createdTrip ->
                navigate(NavigationAction.NavigateToAddParticipants(createdTrip.id.toString()))
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }

    private fun validate(): String? {
        val s = _state.value
        return when {
            s.title.isBlank() -> context.getString(R.string.enter_the_name_of_the_trip)
            s.departureCity.isBlank() -> context.getString(R.string.indicate_the_city_of_departure)
            s.destinationCity.isBlank() -> context.getString(R.string.indicate_the_city_of_destination)
            s.startDate == null || s.endDate == null -> context.getString(R.string.select_the_date_of_the_ttrip)
            s.endDate.isBefore(s.startDate) -> context.getString(R.string.the_end_of_the_end_before_the_start_date)
            else -> null
        }
    }
}