package root.iv.ivandroidacademy.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import root.iv.ivandroidacademy.data.interactor.ActorsInteractor
import root.iv.ivandroidacademy.data.interactor.MovieInteractor
import root.iv.ivandroidacademy.data.model.Actor
import root.iv.ivandroidacademy.data.model.Movie
import root.iv.ivandroidacademy.viewmodel.util.DataState
import timber.log.Timber

class MovieDetailsViewModel(
    private val movieInteractor: MovieInteractor,
    private val actorsInteractor: ActorsInteractor
): ViewModel() {

    private val internalMovie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = internalMovie

    private val internalActors = MutableLiveData<List<Actor>>()
    val actors: LiveData<List<Actor>> = internalActors


    /**
     * Загрузка деталей о фильме и списка актёров:
     *  * Фильм
     *
     *  * Актёры:
     *  1. Отправка запроса в БД. Отображение если что-то вернулось.
     *  2. Отправка запроса в сеть. Если он успешно завершится - обновится кеш и
     */
    fun loadDetails(lifecycleOwner: LifecycleOwner, movieId: Int) = viewModelScope.launch {
        movieInteractor.movie(movieId).observe(lifecycleOwner, Observer { state ->
            when (state) {
                is DataState.Success<Movie> -> internalMovie.postValue(state.data)
                is DataState.Loading -> Timber.i("Loading ${state.progress}")
                is DataState.Error -> Timber.e("Error: ${state.t.localizedMessage}")
            }
        })

        actorsInteractor.cacheActors(movieId).observe(lifecycleOwner, Observer { state ->
            when (state) {
                is DataState.Success -> internalActors.postValue(state.data)
                else -> throw IllegalStateException("")
            }
        })

        actorsInteractor.actors(movieId).observe(lifecycleOwner, Observer { state ->
            when (state) {
                // TODO Реализовать показ загрузки
                is DataState.Loading -> Timber.i("Loading ${state.progress}")
                is DataState.Success -> internalActors.postValue(state.data)
                // TODO Реализовать показ ошибки загрузки
                is DataState.Error -> Timber.e("Error: ${state.t.localizedMessage}")
            }
        })
    }

}