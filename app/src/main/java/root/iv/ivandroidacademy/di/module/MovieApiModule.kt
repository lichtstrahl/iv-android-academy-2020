package root.iv.ivandroidacademy.di.module

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import root.iv.ivandroidacademy.BuildConfig
import root.iv.ivandroidacademy.network.client.MovieDBApi
import root.iv.ivandroidacademy.network.interceptor.ApiKeyInterceptor

@Module
class MovieApiModule {
    private val json = Json { ignoreUnknownKeys = true }

    @Provides
    fun movieDbApi(retrofit: Retrofit): MovieDBApi = retrofit.create()

    @Provides
    fun retrofit(client: OkHttpClient) = Retrofit.Builder()
        .baseUrl(BuildConfig.MOVIE_BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    fun client(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(ApiKeyInterceptor())
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
}