package root.iv.ivandroidacademy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import root.iv.ivandroidacademy.app.App
import root.iv.ivandroidacademy.data.interactor.MovieInteractor
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.data.mediator.RemoteMediatorFactory
import root.iv.ivandroidacademy.data.model.Movie
import timber.log.Timber

class MoviesListViewModel @ExperimentalPagingApi constructor(
    private val movieInteractor: MovieInteractor,
    private val mediatorFactory: RemoteMediatorFactory,
    private val mapper: Mapper
): ViewModel() {

    private val internalMovies = MutableLiveData<PagingData<Movie>>()
    val movies: LiveData<PagingData<Movie>> = internalMovies

    /**
     * Возвращаются фильмы, которые на данный момент есть в БД.
     * Параллельно стартует запрос на получение новых фильмов и обновление их в БД.
     * TODO Не лучшая идея каждый раз очищать БД полностью
     */
    @ExperimentalPagingApi
    fun loadMovies(search: String? = null) = viewModelScope.launch {
        Timber.d("Load movies")
        Pager(
            config = PagingConfig(10),
            remoteMediator = mediatorFactory.moviesMediator()
        ) {
            if (search.isNullOrBlank())
                App.moviesDao.popular()
            else
                movieInteractor.dataSource(search)

         }.flow.map { data -> data.map { mapper.movie(it) } }.cachedIn(viewModelScope).collectLatest { movies ->
            Timber.d("Post movies")
            internalMovies.postValue(movies)
        }
    }
}