package root.iv.ivandroidacademy.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import root.iv.ivandroidacademy.app.App
import root.iv.ivandroidacademy.data.cache.ConfigurationCache
import root.iv.ivandroidacademy.data.cache.GenresCache
import root.iv.ivandroidacademy.data.database.dao.MoviesDao
import root.iv.ivandroidacademy.data.database.entity.MovieEntity
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.network.client.MovieDBApi
import timber.log.Timber
import java.net.ConnectException

@ExperimentalPagingApi
class MoviesMediator(
    private val genresCache: GenresCache,
    private val configurationCache: ConfigurationCache,
    private val movieDBApi: MovieDBApi,
    private val moviesDao: MoviesDao,
    private val mapper: Mapper
): RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, MovieEntity>): MediatorResult = kotlin.runCatching {
            Timber.i("Load [$loadType]: ${state.anchorPosition}")
            // Номер страницы, которую нужно загрузить
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> null
                LoadType.APPEND -> state.anchorPosition?.plus(1)
            } ?: return@runCatching MediatorResult.Success(endOfPaginationReached = true)
            Timber.i("Load page $page")

            val movies = movieDBApi.moviesPopular(page)

            App.database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    Timber.i("Clear cache")
                    moviesDao.clear()
                }

                val entities = movies.data.map { dto ->
                    mapper.entity(mapper.movie(dto, genresCache.get().filter { dto.genreIds.contains(it.id) }, configurationCache.get()))
                }

                Timber.i("Upgrade cache")
                moviesDao.insertAll(entities)
            }

            return@runCatching MediatorResult.Success(movies.page >= movies.pages)
        }.getOrElse { MediatorResult.Error(it) }

}