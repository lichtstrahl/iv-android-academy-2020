package root.iv.ivandroidacademy.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.data.model.Genre
import root.iv.ivandroidacademy.data.model.dto.ImageConfigurationDTO
import root.iv.ivandroidacademy.network.client.MovieDBApi


class DataRepository(
    private val clientApi: MovieDBApi
) {

    private val mapper = Mapper()

    // Cache
    private var genres: List<Genre>? = null
    private var imageConfiguration: ImageConfigurationDTO.Config? = null

    suspend fun movies() = withContext(Dispatchers.IO) {
        clientApi.movies().data.map { mapper.movie(it, genres(it.genreIds), configuration()) }
    }

    suspend fun movie(id: Int) = withContext(Dispatchers.IO) {
        clientApi.movie(id).let { mapper.movie(it, genres(it.genreIds), configuration()) }
    }

    suspend fun actors(movieId: Int) = withContext(Dispatchers.IO) {
        clientApi.actors(movieId).cast.map { mapper.actor(it, configuration()) }
    }

    // ---
    // PRIVATE
    // ---

    private suspend fun genres(ids: List<Int>) = allGenres().filter { ids.contains(it.id) }

    private suspend fun configuration() = imageConfiguration
        ?: clientApi.configuration().config

    private suspend fun allGenres(): List<Genre> = genres
        ?: clientApi.genres().genres.map { mapper.genre(it) }.apply { this@DataRepository.genres = this }

}