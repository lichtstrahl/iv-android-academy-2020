package root.iv.ivandroidacademy.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import root.iv.ivandroidacademy.data.database.entity.MoviesRemoteKeyEntity

@Dao
interface MoviesRemoteKeyDao {

    @Insert(onConflict = REPLACE)
    fun insert(key: MoviesRemoteKeyEntity)

    @Query("SELECT * FROM movies_remote_keys WHERE search = :search")
    fun remoteKeyBySearch(search: String): MoviesRemoteKeyEntity

    @Query("DELETE FROM movies_remote_keys WHERE search = :search")
    fun deleteBySearch(search: String)
}