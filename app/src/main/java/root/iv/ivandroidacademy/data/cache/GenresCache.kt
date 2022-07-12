package root.iv.ivandroidacademy.data.cache

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import root.iv.ivandroidacademy.data.database.dao.GenresDao
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.data.model.Genre
import root.iv.ivandroidacademy.network.client.MovieDBApi

class GenresCache(
    private val movieDBApi: MovieDBApi,
    private val genreDao: GenresDao,
    private val mapper: Mapper
): DatabaseCache<List<Genre>> {

    override suspend fun get(): List<Genre> {
        val cache = genreDao.selectAll().map { mapper.genre(it) }
        return cache.ifEmpty { refresh() }
    }

    override suspend fun refresh(): List<Genre> = withContext(Dispatchers.IO) {
        val genres = movieDBApi.genres().genres.map { mapper.genre(it) }

        genreDao.clear()
        genreDao.insert(genres.map { mapper.entity(it) })

        return@withContext genres
    }
}