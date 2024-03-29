package root.iv.ivandroidacademy.data.interactor

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import root.iv.ivandroidacademy.data.cache.ConfigurationCache
import root.iv.ivandroidacademy.data.cache.GenresCache
import root.iv.ivandroidacademy.data.database.dao.MoviesDao
import root.iv.ivandroidacademy.data.database.entity.MovieEntity
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.data.model.Movie
import root.iv.ivandroidacademy.data.model.dto.MovieDTO
import root.iv.ivandroidacademy.network.client.MovieDBApi
import root.iv.ivandroidacademy.viewmodel.util.DataState
import timber.log.Timber

class MovieInteractor(
    private val genresCache: GenresCache,
    private val configurationCache: ConfigurationCache,
    private val mapper: Mapper,
    private val movieDBApi: MovieDBApi,
    private val moviesDao: MoviesDao
) {

    suspend fun movie(movieId: Int): Flow<DataState<Movie>> = flow {
        emit(DataState.Loading(0))

        kotlin.runCatching {
            val movie = movieDBApi.movie(movieId)
                .let { mapper.movie(it, it.genres(), configurationCache.get()) }
            emit(DataState.Success(movie))
        }.onFailure {
            val movie = moviesDao.movieById(movieId.toLong())
                ?.let { mapper.movie(it) }

            val state = movie
                ?.let { DataState.Success(it) }
                ?: DataState.Error(NoSuchElementException("Couldn't get movie $movieId from network. Not found movie in cache."))
            emit(state)
        }
    }

    /**
     * 1. Какую страницу нужно загрузить? Если значения нет - 1
     * 2. Загружаем указанную страницу
     * 3. Определяем предыдущую страницу
     * 4. Определяем следующую страницу
     * 5. Упаковываем всё это в нужную структуру
     */
    fun dataSource(query: String?) = object : PagingSource<Int, MovieEntity>() {
        override fun getRefreshKey(state: PagingState<Int, MovieEntity>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            }
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieEntity> {
            Timber.d("Load page ${params.key}")
            val page = params.key ?: 1
            val response = if (query.isNullOrBlank()) {
                movieDBApi.moviesPopular(page)
            } else {
                movieDBApi.movies(page, query)
            }
            val prevPage: Int? = if (page > 1) page - 1 else null
            val nextPage: Int? = if (response.pages > page) page + 1 else null

            // TODO Странноватый маппинг из-за RemoteMediator
            val dataEntity = response
                .data
                .map { mapper.movie(it, it.genres(), configurationCache.get()) }
                .map { mapper.entity(it) }
            return LoadResult.Page(dataEntity, prevPage, nextPage)
        }
    }

    // ---
    // PRIVATE
    // ---

    private suspend fun MovieDTO.genres() = genresCache.get().filter { this.genreIds.contains(it.id) }
}