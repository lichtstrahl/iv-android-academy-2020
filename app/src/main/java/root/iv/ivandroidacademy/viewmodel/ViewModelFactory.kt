package root.iv.ivandroidacademy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import root.iv.ivandroidacademy.app.App
import root.iv.ivandroidacademy.data.cache.ConfigurationCache
import root.iv.ivandroidacademy.data.cache.GenresCache
import root.iv.ivandroidacademy.data.interactor.ActorsInteractor
import root.iv.ivandroidacademy.data.interactor.MovieInteractor
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.data.mediator.RemoteMediatorFactory

@ExperimentalPagingApi
object ViewModelFactory: ViewModelProvider.Factory {

    // Caches
    private val genresCache = GenresCache(App.movieDBApi, Mapper)
    private val configurationCache = ConfigurationCache(App.movieDBApi)

    // Interactors
    private val movieInteractor = MovieInteractor(genresCache, configurationCache, Mapper, App.movieDBApi)
    private val actorInteractor = ActorsInteractor(App.movieDBApi, configurationCache, Mapper)

    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        MovieDetailsViewModel::class.java -> MovieDetailsViewModel(
            movieInteractor, actorInteractor
        )

        MoviesListViewModel::class.java -> MoviesListViewModel(
            movieInteractor,
            RemoteMediatorFactory(
                genresCache, configurationCache,
                App.movieDBApi, App.moviesDao,
                Mapper
            ),
            Mapper
        )
        else -> throw IllegalStateException("Not support ViewModel for $modelClass")
    } as T
}