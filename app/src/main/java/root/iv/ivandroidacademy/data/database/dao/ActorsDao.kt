package root.iv.ivandroidacademy.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import root.iv.ivandroidacademy.data.database.entity.ActorEntity

@Dao
interface ActorsDao {

    @Query("SELECT * FROM actors WHERE id = :id")
    suspend fun actor(id: Long): ActorEntity?
}