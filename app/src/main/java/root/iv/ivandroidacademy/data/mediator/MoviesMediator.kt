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
import root.iv.ivandroidacademy.data.database.dao.MoviesRemoteKeyDao
import root.iv.ivandroidacademy.data.database.entity.MovieEntity
import root.iv.ivandroidacademy.data.database.entity.MoviesRemoteKeyEntity
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.network.client.MovieDBApi
import timber.log.Timber

@ExperimentalPagingApi
class MoviesMediator(
    private val genresCache: GenresCache,
    private val configurationCache: ConfigurationCache,
    private val movieDBApi: MovieDBApi,
    private val moviesDao: MoviesDao,
    private val moviesRemoteKeyDao: MoviesRemoteKeyDao,
    private val mapper: Mapper,
    private val search: String?
): RemoteMediator<Int, MovieEntity>() {

    /**
     * При [LoadType.REFRESH] с заданной строкой поиска - не нужно ничего перезагружать.
     * Значит были загружены фильмы через поиск в API
     */
    override suspend fun load(loadType: LoadType, state: PagingState<Int, MovieEntity>): MediatorResult = kotlin.runCatching {
            Timber.i("Load [$loadType]")
            // Номер страницы, которую нужно загрузить
            val page = when (loadType) {
                LoadType.REFRESH -> if (search.isNullOrBlank()) 1 else null
                LoadType.PREPEND -> null
                LoadType.APPEND -> App.database.withTransaction { moviesRemoteKeyDao.remoteKeyBySearch(search?:"") }.nextPage
            } ?: return@runCatching MediatorResult.Success(endOfPaginationReached = true)
            Timber.i("Load page $page")

            val movies = movieDBApi.moviesPopular(page)

            App.database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    Timber.i("Clear cache")
                    moviesDao.clear()
                    moviesRemoteKeyDao.deleteBySearch(search?:"")
                }

                val entities = movies.data.map { dto ->
                    mapper.entity(mapper.movie(dto, genresCache.get().filter { dto.genreIds.contains(it.id) }, configurationCache.get()))
                }

                Timber.i("Upgrade cache")
                moviesDao.insertAll(entities)
                moviesRemoteKeyDao.insert(
                    MoviesRemoteKeyEntity(
                        search?:"",
                        if (movies.page < movies.pages) movies.page+1 else null,
                        if (movies.page > 1) movies.page-1 else null
                    )
                )
            }

            return@runCatching MediatorResult.Success(movies.page >= movies.pages)
        }.getOrElse { MediatorResult.Error(it) }

}