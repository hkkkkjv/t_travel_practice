package ru.kpfu.itis.t_travel.presentation.screens.more

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.kpfu.itis.t_travel.presentation.common.BaseViewModel
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
) : BaseViewModel() {
    fun onEvent(event: MoreEvent) {
        when (event) {
            MoreEvent.OnProfileClick -> navigate(NavigationAction.NavigateToProfile)
            MoreEvent.OnHistoryClick -> navigate(NavigationAction.NavigateToHistory)
            MoreEvent.OnNotificationsClick -> navigate(NavigationAction.NavigateToNotifications)
            MoreEvent.OnDebtsClick -> navigate(NavigationAction.NavigateToDebts)
        }
    }
}