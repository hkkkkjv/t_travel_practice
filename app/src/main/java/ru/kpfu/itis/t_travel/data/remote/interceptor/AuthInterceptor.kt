package ru.kpfu.itis.t_travel.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ru.kpfu.itis.t_travel.presentation.common.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
) : Interceptor {

    companion object {
        private const val TAG = "AuthInterceptor"
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer "
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = tokenManager.getAccessToken()

        return if (!accessToken.isNullOrBlank()) {
            val authenticatedRequest = request.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
            chain.proceed(authenticatedRequest)
        } else {
            chain.proceed(request)
        }
    }
}