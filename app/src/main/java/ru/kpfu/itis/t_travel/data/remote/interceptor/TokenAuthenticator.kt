package ru.kpfu.itis.t_travel.data.remote.interceptor

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.kpfu.itis.t_travel.data.remote.TokenRefreshService
import ru.kpfu.itis.t_travel.presentation.common.settings.TokenManager
import ru.kpfu.itis.t_travel.presentation.navigation.AppNavigator
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import ru.kpfu.itis.t_travel.utils.runCatchingNonCancellation
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val tokenRefreshService: TokenRefreshService,
    private val appNavigator: AppNavigator
) : Authenticator {

    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        val refreshToken = tokenManager.getRefreshToken()
        Log.i("TokenAuthenticator", refreshToken.toString())
        if (refreshToken.isNullOrBlank()) {
            tokenManager.clearTokens()
            navigateToLogin()
            return@runBlocking null
        }
        runCatchingNonCancellation {
            val tokenResponse = tokenRefreshService.refreshTokens(mapOf("refreshToken" to refreshToken))
            Log.i("TokenAuthenticator", tokenResponse.toString())
            when {
                tokenResponse.isSuccessful -> {
                    tokenResponse.body()?.let { auth ->
                        tokenManager.saveTokens(auth.accessToken, auth.refreshToken)
                        response.request.newBuilder()
                            .header("Authorization", "Bearer ${auth.accessToken}")
                            .build()
                    } ?: run {
                        tokenManager.clearTokens()
                        navigateToLogin()
                        null
                    }
                }
                else -> {
                    tokenManager.clearTokens()
                    navigateToLogin()
                    null
                }
            }
        }.getOrNull()
    }
    private suspend fun navigateToLogin() {
        appNavigator.navigate(NavigationAction.NavigateToLogin)
    }
}