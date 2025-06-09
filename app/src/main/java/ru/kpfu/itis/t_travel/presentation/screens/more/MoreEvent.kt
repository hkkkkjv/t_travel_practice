package ru.kpfu.itis.t_travel.presentation.screens.more

sealed class MoreEvent {
    object OnProfileClick : MoreEvent()
    object OnHistoryClick : MoreEvent()
    object OnNotificationsClick : MoreEvent()
    object OnDebtsClick : MoreEvent()
}