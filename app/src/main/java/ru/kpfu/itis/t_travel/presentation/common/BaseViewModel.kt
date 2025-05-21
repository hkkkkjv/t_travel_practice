package ru.kpfu.itis.t_travel.presentation.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction

abstract class BaseViewModel : ViewModel() {
    private val _navigationAction = MutableSharedFlow<NavigationAction>()
    val navigationAction: SharedFlow<NavigationAction> = _navigationAction

    protected suspend fun navigate(action: NavigationAction) {
        _navigationAction.emit(action)
    }
}