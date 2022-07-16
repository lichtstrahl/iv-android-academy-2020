package root.iv.ivandroidacademy.work

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import root.iv.ivandroidacademy.app.App
import root.iv.ivandroidacademy.data.cache.ConfigurationCache
import root.iv.ivandroidacademy.data.cache.GenresCache
import root.iv.ivandroidacademy.data.database.entity.MovieEntity
import root.iv.ivandroidacademy.data.mapper.Mapper
import timber.log.Timber

@ExperimentalPagingApi
class UpdatePopularMoviesWorker(ctx: Context, params: WorkerParameters): CoroutineWorker(ctx, params) {

    // Caches
    private val genresCache = GenresCache(App.movieDBApi, App.genresDao, Mapper)
    private val configurationCache = ConfigurationCache(App.movieDBApi, App.imageConfigDao, Mapper)

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
            .map { page -> App.movieDBApi.moviesPopular(page) }
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
            ?.apply { App.notifyPublisher.notifyPopularMovie(applicationContext, this) }

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
                .apply { this.dbId = App.moviesDao.idByMovieId(this.id) }
                .also {
                    if (it.dbId == null && it.rating > (bestMovie?.rating ?: -1.0)) {
                        bestMovie = it
                    }
                }
        }.also {
            App.moviesDao.insertAll(it)
        }

        return bestMovie
    }
}