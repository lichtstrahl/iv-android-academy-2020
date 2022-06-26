package root.iv.ivandroidacademy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import root.iv.ivandroidacademy.data.interactor.ActorsInteractor
import root.iv.ivandroidacademy.data.interactor.MovieInteractor
import root.iv.ivandroidacademy.data.model.Actor
import root.iv.ivandroidacademy.data.model.Movie

class MovieDetailsViewModel(
    private val movieInteractor: MovieInteractor,
    private val actorsInteractor: ActorsInteractor
): ViewModel() {

    private val internalMovie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = internalMovie

    private val internalActors = MutableLiveData<List<Actor>>()
    val actors: LiveData<List<Actor>> = internalActors

    private val scope = CoroutineScope(Dispatchers.IO)

    fun loadDetails(movieId: Int) {
        scope.launch {
            movieInteractor.movie(movieId)
                ?.also { internalMovie.postValue(it) }
                ?.let { actorsInteractor.actors(movieId) }
                ?.also { internalActors.postValue(it) }
        }
    }
}