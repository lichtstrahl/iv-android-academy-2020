package root.iv.ivandroidacademy.data.interactor

import root.iv.ivandroidacademy.data.cache.ConfigurationCache
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.data.model.Actor
import root.iv.ivandroidacademy.network.client.MovieDBApi

class ActorsInteractor(
    private val movieDBApi: MovieDBApi,
    private val configurationCache: ConfigurationCache,
    private val mapper: Mapper
) {

    suspend fun actors(id: Int): List<Actor> = movieDBApi.actors(id).cast.map { mapper.actor(it, configurationCache.get()) }
}