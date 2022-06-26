package root.iv.ivandroidacademy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import root.iv.ivandroidacademy.app.App
import root.iv.ivandroidacademy.data.cache.ConfigurationCache
import root.iv.ivandroidacademy.data.cache.GenresCache
import root.iv.ivandroidacademy.data.interactor.ActorsInteractor
import root.iv.ivandroidacademy.data.interactor.MovieInteractor
import root.iv.ivandroidacademy.data.mapper.Mapper

object ViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        MovieDetailsViewModel::class.java -> MovieDetailsViewModel(
            MovieInteractor(GenresCache(App.movieDBApi, Mapper), ConfigurationCache(App.movieDBApi), Mapper, App.movieDBApi),
            ActorsInteractor(App.movieDBApi, ConfigurationCache(App.movieDBApi), Mapper)
        )
        else -> throw IllegalStateException("Not support ViewModel for $modelClass")
    } as T
}