package root.iv.ivandroidacademy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import root.iv.ivandroidacademy.data.interactor.MovieInteractor
import root.iv.ivandroidacademy.data.model.Movie

class MoviesListViewModel(
    private val movieInteractor: MovieInteractor
): ViewModel() {

    private val internalMovies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = internalMovies

    private val scope = CoroutineScope(Dispatchers.Main)

    fun loadMovies(search: String? = null) {
        scope.launch {
            val data = movieInteractor.movies(search)
            internalMovies.postValue(data)
        }
    }

    override fun onCleared() {
        scope.cancel()
    }
}