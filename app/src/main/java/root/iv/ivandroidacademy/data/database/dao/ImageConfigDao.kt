package root.iv.ivandroidacademy.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import root.iv.ivandroidacademy.data.database.entity.ImageConfigEntity

@Dao
interface ImageConfigDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(config: ImageConfigEntity)

    @Query("SELECT * FROM image_configuration")
    suspend fun selectAll(): List<ImageConfigEntity>
}