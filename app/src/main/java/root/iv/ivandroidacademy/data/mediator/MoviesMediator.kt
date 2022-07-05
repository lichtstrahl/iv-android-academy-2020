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
import root.iv.ivandroidacademy.data.model.Movie
import root.iv.ivandroidacademy.network.client.MovieDBApi

@ExperimentalPagingApi
class MoviesMediator(
    private val genresCache: GenresCache,
    private val configurationCache: ConfigurationCache,
    private val movieDBApi: MovieDBApi,
    private val moviesDao: MoviesDao,
    private val mapper: Mapper
): RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, MovieEntity>): MediatorResult = try {
        // Номер страницы, которую нужно загрузить
        when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> null
            LoadType.APPEND -> {
                state.anchorPosition?.plus(1)
            }
        }?.let { page ->
            val movies = movieDBApi.moviesPopular(page)

            App.database.withTransaction {
                if (loadType == LoadType.REFRESH)
                    moviesDao.clear()

                val entities = movies.data.map {
                    mapper.entity(mapper.movie(it, genresCache.get(), configurationCache.get()))
                }
                moviesDao.insertAll(entities)
            }

            MediatorResult.Success(movies.page >= movies.pages)
        }
            ?: MediatorResult.Success(endOfPaginationReached = true)

    } catch (e: Exception) {
        MediatorResult.Error(e)
    }
}