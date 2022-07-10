package root.iv.ivandroidacademy.data.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import root.iv.ivandroidacademy.data.cache.ConfigurationCache
import root.iv.ivandroidacademy.data.database.dao.ActorsDao
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.data.model.Actor
import root.iv.ivandroidacademy.network.client.MovieDBApi
import root.iv.ivandroidacademy.viewmodel.util.DataState

class ActorsInteractor(
    private val movieDBApi: MovieDBApi,
    private val actorsDao: ActorsDao,
    private val configurationCache: ConfigurationCache,
    private val mapper: Mapper
) {
    /**
     * Попытка загрузить данные из сети - если неудача, то отображение из кеша.
     */
    suspend fun actors(movieId: Int): Flow<DataState<List<Actor>>> = flow {
        emit(DataState.Loading(0))

        kotlin.runCatching {
            val actors = movieDBApi.actors(movieId)
                .cast
                .map { mapper.actor(it, configurationCache.get()) }
                .also { actors ->
                    actorsDao.insertAll(actors.map { mapper.entity(it, movieId.toLong()) })
                }
            emit(DataState.Success(actors))
        }.onFailure {
            val actors = actorsDao.actorsByMovieId(movieId.toLong())
                .map { mapper.actor(it) }

            val state = if (actors.isEmpty())
                DataState.Error(NoSuchElementException("Couldn't get actors from network. Not found actors in cache."))
            else
                DataState.Success(actors)
            emit(state)
        }
    }
}