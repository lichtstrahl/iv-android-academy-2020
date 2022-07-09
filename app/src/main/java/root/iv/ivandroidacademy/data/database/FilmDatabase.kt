package root.iv.ivandroidacademy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import root.iv.ivandroidacademy.data.database.converter.ListStringConverter
import root.iv.ivandroidacademy.data.database.entity.ActorEntity
import root.iv.ivandroidacademy.data.database.entity.MovieEntity
import root.iv.ivandroidacademy.data.database.dao.ActorsDao
import root.iv.ivandroidacademy.data.database.dao.GenresDao
import root.iv.ivandroidacademy.data.database.dao.ImageConfigDao
import root.iv.ivandroidacademy.data.database.dao.MoviesDao
import root.iv.ivandroidacademy.data.database.entity.GenreEntity
import root.iv.ivandroidacademy.data.database.entity.ImageConfigEntity

@Database(
    entities = [
        MovieEntity::class, ActorEntity::class, ImageConfigEntity::class, GenreEntity::class
               ],
    version = 3
)
@TypeConverters(ListStringConverter::class)
abstract class FilmDatabase: RoomDatabase() {

    abstract fun moviesDao(): MoviesDao
    abstract fun actorsDao(): ActorsDao
    abstract fun imageConfigDao(): ImageConfigDao
    abstract fun genresDao(): GenresDao
}