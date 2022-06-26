package root.iv.ivandroidacademy.app

import android.app.Application
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import root.iv.ivandroidacademy.BuildConfig
import root.iv.ivandroidacademy.network.client.MovieDBApi
import root.iv.ivandroidacademy.network.interceptor.ApiKeyInterceptor
import timber.log.Timber

class App: Application() {

    companion object {
        lateinit var movieDBApi: MovieDBApi
    }

    private val json = Json { ignoreUnknownKeys = true }

    override fun onCreate() {
        super.onCreate()

        initRetrofit()
        initLogging()
    }

    // ---
    // PRIVATE
    // ---

    private fun initRetrofit() {
        val client = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.MOVIE_BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        movieDBApi = retrofit.create()
    }

    private fun initLogging() {
        Timber.plant(Timber.DebugTree())
    }
}