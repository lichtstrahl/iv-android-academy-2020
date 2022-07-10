package root.iv.ivandroidacademy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import root.iv.ivandroidacademy.data.database.converter.ListStringConverter
import root.iv.ivandroidacademy.data.database.dao.*
import root.iv.ivandroidacademy.data.database.entity.*

@Database(
    entities = [
        MovieEntity::class, ActorEntity::class, ImageConfigEntity::class,
        GenreEntity::class, MoviesRemoteKeyEntity::class
               ],
    version = 6
)
@TypeConverters(ListStringConverter::class)
abstract class FilmDatabase: RoomDatabase() {

    abstract fun moviesDao(): MoviesDao
    abstract fun actorsDao(): ActorsDao
    abstract fun imageConfigDao(): ImageConfigDao
    abstract fun genresDao(): GenresDao
    abstract fun moviesRemoteKeyDao(): MoviesRemoteKeyDao
}