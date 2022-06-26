package root.iv.ivandroidacademy.data.interactor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import root.iv.ivandroidacademy.data.cache.ConfigurationCache
import root.iv.ivandroidacademy.data.cache.GenresCache
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.data.model.Movie
import root.iv.ivandroidacademy.data.model.dto.MovieDTO
import root.iv.ivandroidacademy.network.client.MovieDBApi

class MovieInteractor(
    private val genresCache: GenresCache,
    private val configurationCache: ConfigurationCache,
    private val mapper: Mapper,
    private val movieDBApi: MovieDBApi
) {

    suspend fun movie(movieId: Int): Movie? = withContext(Dispatchers.IO) {
        movieDBApi.movie(movieId).let {
            mapper.movie(it, it.genres(), configurationCache.get())
        }
    }

    suspend fun movies(search: String?): List<Movie> = withContext(Dispatchers.IO) {
        val dtos: List<MovieDTO> = if (search.isNullOrBlank())
            movieDBApi.movies().data
        else
            movieDBApi.movies(search).data

        dtos.map { mapper.movie(it, it.genres(), configurationCache.get()) }
    }

    // ---
    // PRIVATE
    // ---

    private suspend fun MovieDTO.genres() = genresCache.get().filter { this.genreIds.contains(it.id) }
}