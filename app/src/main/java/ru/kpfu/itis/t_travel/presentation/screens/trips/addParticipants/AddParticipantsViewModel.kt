package ru.kpfu.itis.t_travel.presentation.screens.trips.addParticipants

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
import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.domain.useCase.trip.participant.AddParticipantsUseCase
import ru.kpfu.itis.t_travel.domain.useCase.trip.participant.GetTripParticipantsUseCase
import ru.kpfu.itis.t_travel.presentation.common.BaseViewModel
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import ru.kpfu.itis.t_travel.presentation.screens.auth.register.RegisterState
import ru.kpfu.itis.t_travel.utils.Constants
import ru.kpfu.itis.t_travel.utils.runSuspendCatching
import javax.inject.Inject

@HiltViewModel
class AddParticipantsViewModel @Inject constructor(
    private val addParticipantsUseCase: AddParticipantsUseCase,
    private val getTripParticipantsUseCase: GetTripParticipantsUseCase,
    @ApplicationContext private val context: Context,
) : BaseViewModel() {

    private var tripId: Int? = null

    fun init(tripId: Int) {
        this.tripId = tripId
        loadParticipants()
    }

    private val _state = MutableStateFlow(AddParticipantsState())
    val state: StateFlow<AddParticipantsState> = _state.asStateFlow()
    fun onEvent(event: AddParticipantsEvent) {
        when (event) {
            is AddParticipantsEvent.ShowAddSheet -> _state.update { it.copy(showAddSheet = true) }
            is AddParticipantsEvent.HideAddSheet -> _state.update {
                it.copy(
                    showAddSheet = false,
                    phoneInput = ""
                )
            }

            is AddParticipantsEvent.PhoneInputChanged -> _state.update { it.copy(phoneInput = event.value) }

            is AddParticipantsEvent.AddClicked -> addParticipant()
            is AddParticipantsEvent.ParticipantAdded -> _state.update {
                it.copy(
                    showAddSheet = false,
                    phoneInput = "",
                    isAdding = false
                )
            }

            is AddParticipantsEvent.Error -> _state.update {
                it.copy(
                    error = event.message,
                    isAdding = false
                )
            }

            is AddParticipantsEvent.ErrorShown -> _state.update {
                it.copy(error = null)
            }

            AddParticipantsEvent.NextClicked -> navigate(NavigationAction.NavigateToAddBudget(tripId = tripId.toString()))
            AddParticipantsEvent.BackClicked -> navigate(NavigationAction.NavigateBack)
        }
    }


    private fun loadParticipants() {
        val id = tripId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            runSuspendCatching { getTripParticipantsUseCase(id, forceRefresh = true) }
                .onSuccess { participants ->
                    _state.update {
                        it.copy(
                            participants = participants.toImmutableList(),
                            isLoading = false
                        )
                    }
                }.onFailure { e ->
                    _state.update { it.copy(error = e.message, isLoading = false) }
                }
        }
    }

    private fun addParticipant() {
        val id = tripId ?: return
        val phone = state.value.phoneInput.trim()
        val currentState = _state.value
        val inputError = validateRegistrationInput(currentState)
        if (inputError != null) {
            onEvent(AddParticipantsEvent.Error(inputError))
            return
        }
        _state.update { it.copy(isAdding = true) }
        viewModelScope.launch {
            val participant = Participant(
                id = 0,
                tripId = id,
                name = phone,
                contact = phone,
                confirmed = false
            )
            runSuspendCatching { addParticipantsUseCase(id, participant) }
                .onSuccess {
                    loadParticipants()
                    onEvent(AddParticipantsEvent.ParticipantAdded)
                }.onFailure {
                    onEvent(AddParticipantsEvent.Error(it.message ?: context.getString(R.string.adding_error)))
                }
        }
    }

    private fun validateRegistrationInput(state: AddParticipantsState): String? {
        return when {
            state.phoneInput.isBlank() -> context.getString(R.string.phone_blank_error)
            !state.phoneInput.matches(Constants.Validation.PHONE_REGEX) -> context.getString(R.string.incorrect_phone_number)
            else -> null

        }
    }
}