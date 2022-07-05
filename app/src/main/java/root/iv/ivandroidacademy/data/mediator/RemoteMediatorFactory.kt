package root.iv.ivandroidacademy.data.mediator

import androidx.paging.ExperimentalPagingApi
import root.iv.ivandroidacademy.data.cache.ConfigurationCache
import root.iv.ivandroidacademy.data.cache.GenresCache
import root.iv.ivandroidacademy.data.database.dao.MoviesDao
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.network.client.MovieDBApi

@ExperimentalPagingApi
class RemoteMediatorFactory(
    private val genresCache: GenresCache,
    private val configurationCache: ConfigurationCache,
    private val movieDBApi: MovieDBApi,
    private val moviesDao: MoviesDao,
    private val mapper: Mapper
) {

    fun moviesMediator() = MoviesMediator(
        genresCache, configurationCache,
        movieDBApi, moviesDao,
        mapper
    )
}