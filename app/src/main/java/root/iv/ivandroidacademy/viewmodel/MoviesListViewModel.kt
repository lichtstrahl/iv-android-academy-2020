package root.iv.ivandroidacademy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import root.iv.ivandroidacademy.data.interactor.MovieInteractor
import root.iv.ivandroidacademy.data.model.Movie
import timber.log.Timber

class MoviesListViewModel(
    private val movieInteractor: MovieInteractor
): ViewModel() {

    private val internalMovies = MutableLiveData<PagingData<Movie>>()
    val movies: LiveData<PagingData<Movie>> = internalMovies

    private val scope = CoroutineScope(Dispatchers.Main)

    fun loadMovies(search: String? = null) {
        scope.launch {
            Timber.d("Load movies")
            Pager(PagingConfig(10)) {
                movieInteractor.dataSource(search)
            }.flow.cachedIn(viewModelScope).collectLatest { movies ->
                Timber.d("Post movies")
                internalMovies.postValue(movies)
            }
        }
    }

    override fun onCleared() {
        scope.cancel()
    }
}