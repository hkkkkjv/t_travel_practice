package ru.kpfu.itis.t_travel.presentation.screens.trips.addParticipants

sealed class AddParticipantsEvent {
    object ShowAddSheet : AddParticipantsEvent()
    object HideAddSheet : AddParticipantsEvent()
    data class PhoneInputChanged(val value: String) : AddParticipantsEvent()
    object AddClicked : AddParticipantsEvent()
    object NextClicked : AddParticipantsEvent()
    object BackClicked : AddParticipantsEvent()
    object ParticipantAdded : AddParticipantsEvent()
    data class Error(val message: String) : AddParticipantsEvent()
    object ErrorShown : AddParticipantsEvent()
}