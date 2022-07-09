package root.iv.ivandroidacademy.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import root.iv.ivandroidacademy.data.database.entity.ActorEntity

@Dao
interface ActorsDao {

    @Query("SELECT * FROM actors WHERE id = :id")
    suspend fun actor(id: Long): ActorEntity?

    @Query("SELECT * FROM actors WHERE id IN (:ids)")
    suspend fun actors(ids: List<Long>): List<ActorEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(actors: List<ActorEntity>)
}