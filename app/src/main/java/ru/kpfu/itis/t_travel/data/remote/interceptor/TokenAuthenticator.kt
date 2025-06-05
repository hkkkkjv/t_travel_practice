package ru.kpfu.itis.t_travel.data.remote.interceptor

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.kpfu.itis.t_travel.data.remote.TokenRefreshService
import ru.kpfu.itis.t_travel.presentation.common.TokenManager
import ru.kpfu.itis.t_travel.utils.runCatchingNonCancellation
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val tokenRefreshService: TokenRefreshService
) : Authenticator {

    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        val refreshToken = tokenManager.getRefreshToken()
        if (refreshToken.isNullOrBlank()) {
            tokenManager.clearTokens()
            return@runBlocking null
        }

        runCatchingNonCancellation {
            val tokenResponse = tokenRefreshService.refreshTokens(mapOf("refreshToken" to refreshToken))
            when {
                tokenResponse.isSuccessful || tokenResponse.code() == 403 -> {
                    tokenResponse.body()?.let { auth ->
                        tokenManager.saveTokens(auth.accessToken, auth.refreshToken)
                        response.request.newBuilder()
                            .header("Authorization", "Bearer ${auth.accessToken}")
                            .build()
                    }
                }
                tokenResponse.code() == 401 -> {
                    tokenManager.clearTokens()
                    null
                }
                else -> null
            }
        }.getOrNull()
    }
}