package root.iv.ivandroidacademy.data.database.repository

import androidx.room.Dao
import androidx.room.Query
import root.iv.ivandroidacademy.data.database.entity.MovieEntity

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies")
    suspend fun popular(): List<MovieEntity>
}