package root.iv.ivandroidacademy.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import root.iv.ivandroidacademy.data.model.Actor
import root.iv.ivandroidacademy.data.model.Movie
import root.iv.ivandroidacademy.data.repository.DataRepository

class MovieDetailsViewModel(
    private val dataRepository: DataRepository
): ViewModel() {

    private val internalMovie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = internalMovie

    private val internalActors = MutableLiveData<List<Actor>>()
    val actors: LiveData<List<Actor>> = internalActors

    private val scope = CoroutineScope(Dispatchers.IO)

    fun loadDetails(movieId: Int) {
        scope.launch {
            dataRepository.use { it.movie(movieId) }
                ?.also { internalMovie.postValue(it) }
                ?.actorIds
                ?.let { dataRepository.use { repo -> repo.actors(it) } }
                ?.also { internalActors.postValue(it) }
        }
    }


    override fun onCleared() {
        super.onCleared()
        dataRepository.close()
    }
}