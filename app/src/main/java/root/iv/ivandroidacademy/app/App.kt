package root.iv.ivandroidacademy.app

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import root.iv.ivandroidacademy.BuildConfig
import root.iv.ivandroidacademy.data.database.FilmDatabase
import root.iv.ivandroidacademy.data.database.dao.*
import root.iv.ivandroidacademy.network.client.MovieDBApi
import root.iv.ivandroidacademy.network.interceptor.ApiKeyInterceptor
import root.iv.ivandroidacademy.ui.notify.NotifyPublisher
import root.iv.ivandroidacademy.work.UpdateCacheWorker
import root.iv.ivandroidacademy.work.UpdatePopularMoviesWorker
import root.iv.ivandroidacademy.work.WorkConstraints
import timber.log.Timber
import java.time.Duration

@ExperimentalPagingApi
class App: Application() {

    companion object {
        lateinit var movieDBApi: MovieDBApi
        lateinit var database: FilmDatabase
        lateinit var notifyPublisher: NotifyPublisher

        val moviesDao: MoviesDao get() = database.moviesDao()
        val actorsDao: ActorsDao get() = database.actorsDao()
        val genresDao: GenresDao get() = database.genresDao()
        val imageConfigDao: ImageConfigDao get() = database.imageConfigDao()
        val moviesRemoteKeyDao: MoviesRemoteKeyDao get() = database.moviesRemoteKeyDao()
    }

    private val json = Json { ignoreUnknownKeys = true }


    override fun onCreate() {
        super.onCreate()

        initRetrofit()
        initLogging()
        initDatabase()
        initUpdateCacheWork()
        initNotify()
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

    private fun initDatabase() {
        database = Room.databaseBuilder(this.applicationContext, FilmDatabase::class.java, "films.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun initUpdateCacheWork() {
        val updateCacheRequest = PeriodicWorkRequestBuilder<UpdateCacheWorker>(Duration.ofHours(24))
            .setConstraints(WorkConstraints.updateCacheConstraint)
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(UpdateCacheWorker.NAME, ExistingPeriodicWorkPolicy.REPLACE, updateCacheRequest)

        val updatePopularMoviesRequest = PeriodicWorkRequestBuilder<UpdatePopularMoviesWorker>(Duration.ofHours(8))
            .setConstraints(WorkConstraints.updateCacheConstraint)
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(UpdatePopularMoviesWorker.NAME, ExistingPeriodicWorkPolicy.REPLACE, updatePopularMoviesRequest)
    }

    private fun initNotify() {
        notifyPublisher = NotifyPublisher(this.applicationContext)
    }
}