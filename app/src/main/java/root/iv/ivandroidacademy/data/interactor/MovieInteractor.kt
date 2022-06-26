package root.iv.ivandroidacademy.data.interactor

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import root.iv.ivandroidacademy.data.cache.ConfigurationCache
import root.iv.ivandroidacademy.data.cache.GenresCache
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.data.model.Movie
import root.iv.ivandroidacademy.data.model.dto.MovieDTO
import root.iv.ivandroidacademy.network.client.MovieDBApi
import timber.log.Timber

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

    /**
     * 1. Какую страницу нужно загрузить? Если значения нет - 1
     * 2. Загружаем указанную страницу
     * 3. Определяем предыдущую страницу
     * 4. Определяем следующую страницу
     * 5. Упаковываем всё это в нужную структуру
     */
    fun dataSource(query: String?) = object : PagingSource<Int, Movie>() {
        override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            }
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
            Timber.d("Load page ${params.key}")
            val page = params.key ?: 1
            val response = if (query.isNullOrBlank()) {
                movieDBApi.moviesPopular(page)
            } else {
                movieDBApi.movies(page, query)
            }
            val prevPage: Int? = if (page > 1) page - 1 else null
            val nextPage: Int? = if (response.pages > page) page + 1 else null

            return LoadResult.Page(response.data.map { mapper.movie(it, it.genres(), configurationCache.get()) }, prevPage, nextPage)
        }
    }

    // ---
    // PRIVATE
    // ---

    private suspend fun MovieDTO.genres() = genresCache.get().filter { this.genreIds.contains(it.id) }
}