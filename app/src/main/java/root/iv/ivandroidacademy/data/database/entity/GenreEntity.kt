package root.iv.ivandroidacademy.data.database.entity

import android.provider.BaseColumns
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey
    @ColumnInfo(name = BaseColumns._ID)
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String
)