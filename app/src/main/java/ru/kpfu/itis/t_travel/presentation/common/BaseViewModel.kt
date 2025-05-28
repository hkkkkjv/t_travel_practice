package ru.kpfu.itis.t_travel.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction

abstract class BaseViewModel : ViewModel() {
    private val _navigationAction = MutableSharedFlow<NavigationAction>()
    val navigationAction: SharedFlow<NavigationAction> = _navigationAction

    protected fun navigate(action: NavigationAction) {
        viewModelScope.launch {
            _navigationAction.emit(action)
        }
    }
}