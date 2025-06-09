package ru.kpfu.itis.t_travel.domain.useCase.auth

import android.util.Log
import ru.kpfu.itis.t_travel.data.local.prefs.TokenPreferences
import ru.kpfu.itis.t_travel.data.model.DeviceTokenRequest
import ru.kpfu.itis.t_travel.data.remote.ApiService
import javax.inject.Inject

class SendFcmTokenUseCase @Inject constructor(
    private val apiService: ApiService,
    private val tokenPreferences: TokenPreferences
) {
    suspend operator fun invoke() {
        Log.i("SendFcmTokenUseCase", tokenPreferences.fcmToken.toString())
        tokenPreferences.fcmToken?.let { token ->
            apiService.registerDeviceToken(DeviceTokenRequest(token))
        }
    }
}