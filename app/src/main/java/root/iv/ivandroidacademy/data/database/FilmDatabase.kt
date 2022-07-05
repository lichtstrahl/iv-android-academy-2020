package root.iv.ivandroidacademy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import root.iv.ivandroidacademy.data.database.entity.ActorEntity
import root.iv.ivandroidacademy.data.database.entity.MovieEntity
import root.iv.ivandroidacademy.data.database.dao.ActorsDao
import root.iv.ivandroidacademy.data.database.dao.MoviesDao

@Database(
    entities = [
        MovieEntity::class, ActorEntity::class
               ],
    version = 1
)
abstract class FilmDatabase: RoomDatabase() {

    abstract fun moviesDao(): MoviesDao
    abstract fun actorsDao(): ActorsDao
}