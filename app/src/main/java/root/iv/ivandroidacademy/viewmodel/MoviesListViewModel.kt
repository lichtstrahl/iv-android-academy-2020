package root.iv.ivandroidacademy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import root.iv.ivandroidacademy.app.App
import root.iv.ivandroidacademy.data.database.entity.MovieEntity
import root.iv.ivandroidacademy.data.interactor.MovieInteractor
import root.iv.ivandroidacademy.data.mediator.MoviesMediator
import root.iv.ivandroidacademy.data.model.Movie
import timber.log.Timber

class MoviesListViewModel @ExperimentalPagingApi constructor(
    private val movieInteractor: MovieInteractor,
    private val moviesMediator: MoviesMediator
): ViewModel() {

    private val internalMovies = MutableLiveData<PagingData<MovieEntity>>()
    val movies: LiveData<PagingData<MovieEntity>> = internalMovies

    private val scope = CoroutineScope(Dispatchers.Main)

    /**
     * Возвращаются фильмы, которые на данный момент есть в БД.
     * Параллельно стартует запрос на получение новых фильмов и обновление их в БД.
     * TODO Не лучшая идея каждый раз очищать БД полностью
     */
    @ExperimentalPagingApi
    fun loadMovies(search: String? = null) = scope.launch {


        Timber.d("Load movies")
        Pager(
            config = PagingConfig(10),
            remoteMediator = moviesMediator
        ) {
            App.moviesDao.popular()
         }.flow.cachedIn(viewModelScope).collectLatest { movies ->
            Timber.d("Post movies")
            internalMovies.postValue(movies)
        }
    }

    override fun onCleared() {
        scope.cancel()
    }
}