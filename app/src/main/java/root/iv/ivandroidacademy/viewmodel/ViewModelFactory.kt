package root.iv.ivandroidacademy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import root.iv.ivandroidacademy.data.cache.ConfigurationCache
import root.iv.ivandroidacademy.data.cache.GenresCache
import root.iv.ivandroidacademy.data.interactor.ActorsInteractor
import root.iv.ivandroidacademy.data.interactor.MovieInteractor
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.data.mediator.RemoteMediatorFactory
import root.iv.ivandroidacademy.di.component.GlobalComponent

@ExperimentalPagingApi
class ViewModelFactory(
    private val globalComponent: GlobalComponent
): ViewModelProvider.Factory {

    // Caches
    private val genresCache = GenresCache(globalComponent.movieDbApi(), globalComponent.genresDao(), Mapper)
    private val configurationCache = ConfigurationCache(globalComponent.movieDbApi(), globalComponent.imageConfigDao(), Mapper)

    // Interactors
    private val movieInteractor = MovieInteractor(genresCache, configurationCache, Mapper, globalComponent.movieDbApi(), globalComponent.moviesDao())
    private val actorInteractor = ActorsInteractor(
        globalComponent.movieDbApi(), globalComponent.actorsDao(), configurationCache, Mapper
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        MovieDetailsViewModel::class.java -> MovieDetailsViewModel(
            movieInteractor, actorInteractor
        )

        MoviesListViewModel::class.java -> MoviesListViewModel(
            movieInteractor,
            RemoteMediatorFactory(
                genresCache, configurationCache,
                globalComponent.movieDbApi(), globalComponent.moviesDao(), globalComponent.remoteKeysDao(),
                globalComponent.database(),
                Mapper
            ),
            globalComponent.moviesDao(),
            Mapper
        )
        else -> throw IllegalStateException("Not support ViewModel for $modelClass")
    } as T
}