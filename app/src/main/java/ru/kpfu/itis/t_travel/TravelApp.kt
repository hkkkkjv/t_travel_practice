package ru.kpfu.itis.t_travel

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.kpfu.itis.t_travel.presentation.common.settings.AppSettingsManager
import javax.inject.Inject

@HiltAndroidApp
class TravelApp:Application(){
    @Inject
    lateinit var appSettingsManager: AppSettingsManager

    override fun onCreate() {
        super.onCreate()
        appSettingsManager.initializeAppSettings()
    }
}