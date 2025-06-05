package ru.kpfu.itis.t_travel.presentation.navigation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNavigator @Inject constructor() {
    private val _navigationAction = MutableSharedFlow<NavigationAction>()
    val navigationAction: Flow<NavigationAction> = _navigationAction.asSharedFlow()

    suspend fun navigate(navigationAction: NavigationAction) {
        _navigationAction.emit(navigationAction)
    }
}