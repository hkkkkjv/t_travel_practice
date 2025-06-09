package ru.kpfu.itis.t_travel.data.remote.interceptor

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.kpfu.itis.t_travel.presentation.common.settings.FavoriteTripManager
import ru.kpfu.itis.t_travel.presentation.common.settings.ProfileManager
import ru.kpfu.itis.t_travel.presentation.common.settings.TokenManager
import ru.kpfu.itis.t_travel.presentation.navigation.AppNavigator
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val appNavigator: AppNavigator,
    private val favoriteTripManager: FavoriteTripManager,
    private val profileManager: ProfileManager
) : Interceptor {

    companion object {
        private const val TAG = "AuthInterceptor"
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer "
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = tokenManager.getAccessToken()

        val response = if (!accessToken.isNullOrBlank()) {
            val authenticatedRequest = request.newBuilder()
                .header(AUTHORIZATION_HEADER, "$BEARER_PREFIX$accessToken")
                .build()
            chain.proceed(authenticatedRequest)
        } else {
            chain.proceed(request)
        }
        if (response.code == 403) {
            runBlocking {
                tokenManager.clearTokens()
                favoriteTripManager.clearFavoriteTrip()
                profileManager.clearAvatarUri()
                appNavigator.navigate(NavigationAction.NavigateToLogin)

            }
        }
        return response
    }
}