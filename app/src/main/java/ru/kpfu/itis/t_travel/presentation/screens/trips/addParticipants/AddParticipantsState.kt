package ru.kpfu.itis.t_travel.presentation.screens.trips.addParticipants

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kpfu.itis.t_travel.domain.model.Participant

@Immutable
data class AddParticipantsState(
    val participants: ImmutableList<Participant> = persistentListOf(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showAddSheet: Boolean = false,
    val phoneInput: String = "",
    val isAdding: Boolean = false
)