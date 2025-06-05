package ru.kpfu.itis.t_travel.presentation.screens.trips.create

import java.time.LocalDate

data class NewTripState(
    val title: String = "",
    val description: String = "",
    val departureCity: String = "",
    val destinationCity: String = "",
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDatePickerStart: Boolean = false,
    val showDatePickerEnd: Boolean = false,
    )