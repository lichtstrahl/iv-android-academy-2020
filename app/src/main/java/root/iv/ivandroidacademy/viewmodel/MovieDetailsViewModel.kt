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

    private val internalNetworkError = MutableLiveData<String>()
    val networkError: LiveData<String> = internalNetworkError


    /**
     * Загрузка деталей о фильме и списка актёров:
     *  * Фильм
     *  1. Отправка запроса в БД. Отображение если что-то вернулось.
     *  2. Отправка запроса в сеть. Если успех - обновится кеш и отобразятся новые данные.
     *  * Актёры:
     *  1. Отправка запроса в БД. Отображение если что-то вернулось.
     *  2. Отправка запроса в сеть. Если успех - обновится кеш и отобразятся новые данные.
     */
    fun loadDetails(lifecycleOwner: LifecycleOwner, movieId: Int) = viewModelScope.launch {

        movieInteractor.cacheMovie(movieId).observe(lifecycleOwner, Observer { state ->
            when (state) {
                is DataState.Success<Movie> -> internalMovie.postValue(state.data!!)
                else -> throw IllegalStateException()
            }
        })

        movieInteractor.movie(movieId).observe(lifecycleOwner, Observer { state ->
            when (state) {
                is DataState.Loading -> Timber.i("Loading ${state.progress}")
                is DataState.Success<Movie> -> internalMovie.postValue(state.data!!)
                is DataState.Error -> internalNetworkError.postValue(state.t.localizedMessage)
            }
        })

        actorsInteractor.cacheActors(movieId).observe(lifecycleOwner, Observer { state ->
            when (state) {
                is DataState.Success -> internalActors.postValue(state.data!!)
                else -> throw IllegalStateException("")
            }
        })

        actorsInteractor.actors(movieId).observe(lifecycleOwner, Observer { state ->
            when (state) {
                // TODO Реализовать показ загрузки
                is DataState.Loading -> Timber.i("Loading ${state.progress}")
                is DataState.Success -> internalActors.postValue(state.data!!)
                is DataState.Error -> internalNetworkError.postValue(state.t.localizedMessage)
            }
        })
    }

}