package root.iv.ivandroidacademy.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import root.iv.ivandroidacademy.data.database.FilmDatabase
import root.iv.ivandroidacademy.data.database.dao.*

@Module(
    includes = [ApplicationModule::class]
)
class DatabaseModule {

    @Provides
    fun moviesDao(database: FilmDatabase): MoviesDao = database.moviesDao()

    @Provides
    fun actorsDao(database: FilmDatabase): ActorsDao = database.actorsDao()

    @Provides
    fun genresDao(database: FilmDatabase): GenresDao = database.genresDao()

    @Provides
    fun imageConfigDao(database: FilmDatabase): ImageConfigDao = database.imageConfigDao()

    @Provides
    fun moviesRemoteKeyDao(database: FilmDatabase): MoviesRemoteKeyDao = database.moviesRemoteKeyDao()

    @Provides
    fun database(ctx: Context): FilmDatabase = Room.databaseBuilder(ctx, FilmDatabase::class.java, "films.db")
        .fallbackToDestructiveMigration()
        .build()
}