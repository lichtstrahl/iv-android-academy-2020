package root.iv.ivandroidacademy.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import root.iv.ivandroidacademy.app.App
import root.iv.ivandroidacademy.data.cache.ConfigurationCache
import root.iv.ivandroidacademy.data.cache.DatabaseCache
import root.iv.ivandroidacademy.data.cache.GenresCache
import root.iv.ivandroidacademy.data.mapper.Mapper

/**
 * Обновление доступных кешей в приложении
 */
class UpdateCacheWorker(ctx: Context, params: WorkerParameters): CoroutineWorker(ctx, params) {

    private val caches: List<DatabaseCache<*>> = listOf(
        ConfigurationCache(App.movieDBApi, App.imageConfigDao, Mapper),
        GenresCache(App.movieDBApi, App.genresDao, Mapper)
    )

    companion object {
        const val NAME = "update-cache-work"
    }

    override suspend fun doWork(): Result = kotlin.runCatching {
        caches.forEach { it.refresh() }
        Result.success()
    }.getOrElse { Result.failure() }


}