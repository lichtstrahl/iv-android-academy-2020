package root.iv.ivandroidacademy.data.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
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

    suspend fun cacheActors(movieId: Int): LiveData<DataState<List<Actor>>> = liveData {
        val dataState = actorsDao.actorsByMovieId(movieId.toLong())
            .map { mapper.actor(it) }
            .let { DataState.Success(it) }
        emit(dataState)
    }

    suspend fun actors(movieId: Int): LiveData<DataState<List<Actor>>> = liveData {
        emit(DataState.Loading(0))
        val dataState = kotlin.runCatching {
            movieDBApi.actors(movieId)
                .cast
                .map { mapper.actor(it, configurationCache.get()) }
                .also { actors ->
                    actorsDao.insertAll(actors.map {
                        mapper.entity(
                            it,
                            movieId.toLong()
                        )
                    })
                }
                .let { DataState.Success(it) }
        }.getOrElse { DataState.Error(it) }
        emit(dataState)
    }
}