package root.iv.ivandroidacademy.app

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.serialization.json.Json
import root.iv.ivandroidacademy.di.component.DaggerGlobalComponent
import root.iv.ivandroidacademy.di.component.GlobalComponent
import root.iv.ivandroidacademy.di.module.ApplicationModule
import root.iv.ivandroidacademy.work.UpdateCacheWorker
import root.iv.ivandroidacademy.work.UpdatePopularMoviesWorker
import root.iv.ivandroidacademy.work.WorkConstraints
import timber.log.Timber
import java.time.Duration

@ExperimentalPagingApi
class App: Application() {

    companion object {
        lateinit var globalComponent: GlobalComponent
    }

    override fun onCreate() {
        super.onCreate()

        initLogging()
        initUpdateCacheWork()
        initDI()
    }

    // ---
    // PRIVATE
    // ---

    private fun initLogging() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initUpdateCacheWork() {
        val updateCacheRequest = PeriodicWorkRequestBuilder<UpdateCacheWorker>(Duration.ofHours(24))
            .setConstraints(WorkConstraints.updateCacheConstraint)
            .setInitialDelay(Duration.ofSeconds(15))
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(UpdateCacheWorker.NAME, ExistingPeriodicWorkPolicy.REPLACE, updateCacheRequest)

        val updatePopularMoviesRequest = PeriodicWorkRequestBuilder<UpdatePopularMoviesWorker>(Duration.ofHours(8))
            .setConstraints(WorkConstraints.updateCacheConstraint)
            .setInitialDelay(Duration.ofSeconds(15))
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(UpdatePopularMoviesWorker.NAME, ExistingPeriodicWorkPolicy.REPLACE, updatePopularMoviesRequest)
    }

    private fun initDI() {
        globalComponent = DaggerGlobalComponent.builder()
            .applicationModule(ApplicationModule(this.applicationContext))
            .build()
    }
}