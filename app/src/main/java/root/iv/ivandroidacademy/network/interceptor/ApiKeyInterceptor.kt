package root.iv.ivandroidacademy.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import root.iv.ivandroidacademy.BuildConfig

class ApiKeyInterceptor: Interceptor {

    companion object {
        private const val API_KEY_PARAM = "api_key"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val url = original.url.newBuilder()
            .addQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_API_KEY)
            .build()

        return original.newBuilder()
            .url(url)
            .build()
            .let(chain::proceed)
    }
}