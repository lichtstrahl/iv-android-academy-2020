package root.iv.ivandroidacademy.data.mediator

import androidx.paging.ExperimentalPagingApi
import root.iv.ivandroidacademy.data.cache.ConfigurationCache
import root.iv.ivandroidacademy.data.cache.GenresCache
import root.iv.ivandroidacademy.data.database.FilmDatabase
import root.iv.ivandroidacademy.data.database.dao.MoviesDao
import root.iv.ivandroidacademy.data.database.dao.MoviesRemoteKeyDao
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.network.client.MovieDBApi

@ExperimentalPagingApi
class RemoteMediatorFactory(
    private val genresCache: GenresCache,
    private val configurationCache: ConfigurationCache,
    private val movieDBApi: MovieDBApi,
    private val moviesDao: MoviesDao,
    private val moviesRemoteKeyDao: MoviesRemoteKeyDao,
    private val database: FilmDatabase,
    private val mapper: Mapper
) {

    fun moviesMediator(search: String?) = MoviesMediator(
        genresCache, configurationCache,
        movieDBApi, moviesDao, moviesRemoteKeyDao,
        mapper, database, search
    )
}