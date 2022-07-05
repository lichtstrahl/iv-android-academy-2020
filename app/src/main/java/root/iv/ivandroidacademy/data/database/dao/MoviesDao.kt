package root.iv.ivandroidacademy.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import root.iv.ivandroidacademy.data.database.entity.MovieEntity

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies")
    fun popular(): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM movies")
    suspend fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)
}