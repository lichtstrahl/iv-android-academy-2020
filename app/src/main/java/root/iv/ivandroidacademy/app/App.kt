package root.iv.ivandroidacademy.app

import android.app.Application
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import root.iv.ivandroidacademy.BuildConfig
import root.iv.ivandroidacademy.data.database.FilmDatabase
import root.iv.ivandroidacademy.data.database.dao.ActorsDao
import root.iv.ivandroidacademy.data.database.dao.MoviesDao
import root.iv.ivandroidacademy.network.client.MovieDBApi
import root.iv.ivandroidacademy.network.interceptor.ApiKeyInterceptor
import root.iv.ivandroidacademy.network.interceptor.ConnectivityInterceptor
import timber.log.Timber

class App: Application() {

    companion object {
        lateinit var movieDBApi: MovieDBApi
        lateinit var database: FilmDatabase

        val moviesDao: MoviesDao get() = database.moviesDao()
        val actorsDao: ActorsDao get() = database.actorsDao()
    }

    private val json = Json { ignoreUnknownKeys = true }


    override fun onCreate() {
        super.onCreate()

        initRetrofit()
        initLogging()
        initDatabase()
    }

    // ---
    // PRIVATE
    // ---

    private fun initRetrofit() {
        val client = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(ConnectivityInterceptor(this.applicationContext))
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

    private fun initDatabase() {
        database = Room.databaseBuilder(this.applicationContext, FilmDatabase::class.java, "films.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}