package ru.kpfu.itis.t_travel.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kpfu.itis.t_travel.presentation.navigation.AppNavigator
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {
    @Inject
    lateinit var appNavigator: AppNavigator
    protected fun navigate(action: NavigationAction) {
        viewModelScope.launch {
            appNavigator.navigate(action)
        }
    }
}