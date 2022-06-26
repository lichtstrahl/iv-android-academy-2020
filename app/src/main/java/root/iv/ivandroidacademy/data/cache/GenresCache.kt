package root.iv.ivandroidacademy.data.cache

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.data.model.Genre
import root.iv.ivandroidacademy.network.client.MovieDBApi

class GenresCache(
    movieDBApi: MovieDBApi,
    mapper: Mapper
): Cache<List<Genre>>({
    withContext(Dispatchers.IO) {
        movieDBApi.genres().genres.map { mapper.genre(it) }
    }
})