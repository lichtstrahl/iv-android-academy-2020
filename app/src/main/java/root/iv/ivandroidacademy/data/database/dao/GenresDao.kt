package root.iv.ivandroidacademy.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import root.iv.ivandroidacademy.data.database.entity.GenreEntity

@Dao
interface GenresDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(genre: List<GenreEntity>)

    @Query("SELECT * FROM genres")
    suspend fun selectAll(): List<GenreEntity>

    @Query("DELETE FROM genres")
    suspend fun clear()
}