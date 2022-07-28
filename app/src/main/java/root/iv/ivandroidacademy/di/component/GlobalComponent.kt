package root.iv.ivandroidacademy.di.component

import android.content.Context
import dagger.Component
import root.iv.ivandroidacademy.data.database.FilmDatabase
import root.iv.ivandroidacademy.data.database.dao.*
import root.iv.ivandroidacademy.di.module.ApplicationModule
import root.iv.ivandroidacademy.di.module.DatabaseModule
import root.iv.ivandroidacademy.di.module.MovieApiModule
import root.iv.ivandroidacademy.network.client.MovieDBApi

@Component(
    modules = [MovieApiModule::class, DatabaseModule::class]
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

    companion object {
        fun component(ctx: Context): GlobalComponent = DaggerGlobalComponent.builder()
            .applicationModule(ApplicationModule(ctx))
            .build()
    }
}