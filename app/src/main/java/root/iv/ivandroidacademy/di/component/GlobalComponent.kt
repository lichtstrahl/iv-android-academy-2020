package root.iv.ivandroidacademy.di.component

import androidx.paging.ExperimentalPagingApi
import dagger.Component
import root.iv.ivandroidacademy.data.database.FilmDatabase
import root.iv.ivandroidacademy.data.database.dao.*
import root.iv.ivandroidacademy.di.module.DatabaseModule
import root.iv.ivandroidacademy.di.module.MovieApiModule
import root.iv.ivandroidacademy.di.module.NotifyModule
import root.iv.ivandroidacademy.network.client.MovieDBApi
import root.iv.ivandroidacademy.ui.notify.NotifyPublisher

@ExperimentalPagingApi
@Component(
    modules = [MovieApiModule::class, DatabaseModule::class, NotifyModule::class]
)
interface GlobalComponent {

    // Retrofit
    fun movieDbApi(): MovieDBApi

    // Database
    fun database(): FilmDatabase
    fun actorsDao(): ActorsDao
    fun moviesDao(): MoviesDao
    fun imageConfigDao(): ImageConfigDao
    fun remoteKeysDao(): MoviesRemoteKeyDao
    fun genresDao(): GenresDao

    // Notify
    fun notifyPublisher(): NotifyPublisher
}