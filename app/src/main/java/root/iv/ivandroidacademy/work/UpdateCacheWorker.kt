package root.iv.ivandroidacademy.work

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import root.iv.ivandroidacademy.data.cache.ConfigurationCache
import root.iv.ivandroidacademy.data.cache.DatabaseCache
import root.iv.ivandroidacademy.data.cache.GenresCache
import root.iv.ivandroidacademy.data.database.dao.GenresDao
import root.iv.ivandroidacademy.data.database.dao.ImageConfigDao
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.network.client.MovieDBApi
import javax.inject.Inject

/**
 * Обновление доступных кешей в приложении
 */
@ExperimentalPagingApi
class UpdateCacheWorker(ctx: Context, params: WorkerParameters): CoroutineWorker(ctx, params) {

    @Inject
    lateinit var movieDBApi: MovieDBApi
    @Inject
    lateinit var imageConfigDao: ImageConfigDao
    @Inject
    lateinit var genresDao: GenresDao

    private val caches: List<DatabaseCache<*>> = listOf(
        ConfigurationCache(movieDBApi, imageConfigDao, Mapper),
        GenresCache(movieDBApi, genresDao, Mapper)
    )

    companion object {
        const val NAME = "update-cache-work"
    }

    override suspend fun doWork(): Result = kotlin.runCatching {
        caches.forEach { it.refresh() }
        Result.success()
    }.getOrElse { Result.failure() }


}