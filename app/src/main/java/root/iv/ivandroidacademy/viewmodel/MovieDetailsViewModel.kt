package root.iv.ivandroidacademy.viewmodel

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val internalError = MutableLiveData<String>()
    val error: LiveData<String> = internalError


    private val internalActorsLoading = MutableLiveData<ActorsLoadingInfo>()
    val actorsLoading: LiveData<ActorsLoadingInfo> = internalActorsLoading

    fun loadDetails(movieId: Int) = viewModelScope.launch {
        movieInteractor.movie(movieId).collect { state ->
            when (state) {
                is DataState.Loading -> Timber.i("Loading ${state.progress}")
                is DataState.Success -> state.data.also { internalMovie.value = it }
                is DataState.Error -> internalError.postValue(state.t.localizedMessage)
            }
        }

        actorsInteractor.actors(movieId).collect { state ->
            when (state) {
                is DataState.Loading -> internalActorsLoading.postValue(ActorsLoadingInfo.loading())
                is DataState.Success -> {
                    state.data.let { internalActors.postValue(it) }
                    internalActorsLoading.postValue(ActorsLoadingInfo.loaded())
                }
                is DataState.Error -> {
                    internalError.postValue(state.t.localizedMessage)
                    internalActorsLoading.postValue(ActorsLoadingInfo.loaded())
                }
            }
        }
    }

    fun scheduleEvent(ctx: Context, time: Long, movie: Movie) {
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, time)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, time + movie.duration!!*60*1000)
            .putExtra(CalendarContract.Events.TITLE, movie.title)
            .putExtra(CalendarContract.Events.DESCRIPTION, movie.storyline)
            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
        ctx.startActivity(intent)
    }

    /**
     * Необходимая информация для отображения загрузки списка актёров
     */
    data class ActorsLoadingInfo(
        val actorsListViewVisible: Int,
        val actorsLoaderVisible: Int
    ) {
        companion object {
            fun loading() = ActorsLoadingInfo(View.INVISIBLE, View.VISIBLE)
            fun loaded() = ActorsLoadingInfo(View.VISIBLE, View.GONE)
        }
    }
}