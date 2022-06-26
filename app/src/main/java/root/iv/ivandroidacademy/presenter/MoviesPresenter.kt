package root.iv.ivandroidacademy.presenter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import root.iv.ivandroidacademy.data.interactor.MovieInteractor
import root.iv.ivandroidacademy.data.model.Movie

class MoviesPresenter(
    private val moviesInteractor: MovieInteractor
): Presenter<MoviesPresenter.View> {

    private val scope = CoroutineScope(Dispatchers.Main)
    private var view: View? = null

    fun loadMovies(search: String? = null) {
        view?.viewLoadingMovies()
        scope.launch {
            val movies = moviesInteractor.movies(search)
            view?.viewMoviesList(movies)
        }
    }

    /**
     * View должно уметь:
     * 1. Отображать список фильмов
     * 2. Отображать индикатор того, что фильмы грузятся
     */
    interface View {
        fun viewMoviesList(movies: List<Movie>)
        fun viewLoadingMovies()
    }

    override fun attach(view: View) {
        this.view = view
    }

    override fun detach() {
        this.view = null
    }

    override fun cancel() {
        this.scope.cancel()
    }
}