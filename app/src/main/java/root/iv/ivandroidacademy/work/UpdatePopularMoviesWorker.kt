package root.iv.ivandroidacademy.work

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import root.iv.ivandroidacademy.app.App
import root.iv.ivandroidacademy.data.cache.ConfigurationCache
import root.iv.ivandroidacademy.data.cache.GenresCache
import root.iv.ivandroidacademy.data.database.dao.GenresDao
import root.iv.ivandroidacademy.data.database.dao.ImageConfigDao
import root.iv.ivandroidacademy.data.database.dao.MoviesDao
import root.iv.ivandroidacademy.data.database.entity.MovieEntity
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.network.client.MovieDBApi
import root.iv.ivandroidacademy.ui.notify.NotifyPublisher
import timber.log.Timber
import javax.inject.Inject

/**
 * При первом объявлении работы необходимо учитывать параллельный показ списка фильмов, т.е. переобновление БД.
 * Из-за чего могут приходить фильмы, которые просто не успели ещё записаться в БД.
 * Поэтому при инициализации работы следует задать init-delay ~10-15 секунд.
 */
@ExperimentalPagingApi
class UpdatePopularMoviesWorker(ctx: Context, params: WorkerParameters): CoroutineWorker(ctx, params) {


    @Inject
    lateinit var movieDbApi: MovieDBApi
    @Inject
    lateinit var notifyPublisher: NotifyPublisher
    @Inject
    lateinit var moviesDao: MoviesDao
    @Inject
    lateinit var genresDao: GenresDao
    @Inject
    lateinit var imageConfigDao: ImageConfigDao

    // Caches
    private val genresCache = GenresCache(movieDbApi, genresDao, Mapper)
    private val configurationCache = ConfigurationCache(movieDbApi, imageConfigDao, Mapper)

    companion object {
        const val NAME = "update-popular-movies-work"
        private const val UPDATE_PAGE_COUNT = 5
    }

    /**
     * Получаем заданное количество страниц с новыми популярными фильмами.
     * Преобразумем в формат Entity и обновляем информацию в БД:
     *  * Если фильм уже существовал - перезаписываем его
     *  * Если фильма не было - добавляем
     */
    override suspend fun doWork(): Result = kotlin.runCatching {
        Timber.d("Update popular movies ($UPDATE_PAGE_COUNT pages)")
        val bestMovies = mutableListOf<MovieEntity>()

        (1..UPDATE_PAGE_COUNT)
            .map { page -> movieDbApi.moviesPopular(page) }
            .map { movies ->
                movies.data.map { dto ->
                    val genres = genresCache.get().filter { g -> dto.genreIds.contains(g.id) }
                    Mapper.movie(dto, genres, configurationCache.get())
                }
            }
            .map { movies -> movies.map { Mapper.entity(it) } }
            .forEach { entities -> entities.replaceInCache()?.let { bestMovies.add(it) } }

        bestMovies
            .maxByOrNull { it.rating }
            ?.apply { notifyPublisher.notifyPopularMovie(applicationContext, this) }

        Timber.d("Updated successful")
        Result.success()
    }.getOrElse {
        Timber.e("Update failed: ${it.localizedMessage}")
        it.printStackTrace()
        Result.failure()
    }

    // ---
    // PRIVATE
    // ---

    /**
     * @return Новый самый рейтинговый фильм
     */
    private suspend fun List<MovieEntity>.replaceInCache(): MovieEntity? {
        var bestMovie: MovieEntity? = null
        this.map { entity ->
            entity
                .apply { this.dbId = moviesDao.idByMovieId(this.id) }
                .also {
                    if (it.dbId == null && it.rating > (bestMovie?.rating ?: -1.0)) {
                        bestMovie = it
                    }
                }
        }.also {
            moviesDao.insertAll(it)
        }

        if (bestMovie == null)
            Timber.d("Not found new movie")
        else
            Timber.d("Found new movie ${bestMovie!!.title}")

        return bestMovie
    }
}