package root.iv.ivandroidacademy.data.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import root.iv.ivandroidacademy.data.database.entity.MovieEntity

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies ORDER BY _id")
    fun popular(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun movieById(movieId: Long): MovieEntity?

    @Query("SELECT * FROM movies WHERE id IN (:movieIds)")
    suspend fun moviesByIds(movieIds: List<Long>): List<MovieEntity>

    @Query("SELECT _id FROM movies WHERE id = :movieId")
    suspend fun idByMovieId(movieId: Long): Long?

    @Update
    suspend fun update(entity: MovieEntity)

    @Query("DELETE FROM movies")
    suspend fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)
}