package ru.kpfu.itis.t_travel.presentation.screens.trips.create

import java.time.LocalDate

sealed class NewTripEvent {
    data class TitleChanged(val value: String) : NewTripEvent()
    data class DescriptionChanged(val value: String) : NewTripEvent()
    data class DepartureCityChanged(val value: String) : NewTripEvent()
    data class DestinationCityChanged(val value: String) : NewTripEvent()
    data class StartDateChanged(val value: LocalDate) : NewTripEvent()
    data class EndDateChanged(val value: LocalDate) : NewTripEvent()
    data class StartDateSelected(val date: LocalDate) : NewTripEvent()
    data class EndDateSelected(val date: LocalDate) : NewTripEvent()
    data class DateRangeChanged(val start: LocalDate, val end: LocalDate) : NewTripEvent()
    object CreateTripClicked : NewTripEvent()
    object BackClicked : NewTripEvent()
    object ErrorShown : NewTripEvent()
    object ShowStartDatePicker : NewTripEvent()
    object ShowEndDatePicker : NewTripEvent()
    object HideDatePickers : NewTripEvent()
}