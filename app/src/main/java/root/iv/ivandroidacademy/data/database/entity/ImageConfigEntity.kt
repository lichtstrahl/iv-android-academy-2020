package root.iv.ivandroidacademy.data.database.entity

import android.provider.BaseColumns
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_configuration")
data class ImageConfigEntity(
    @PrimaryKey
    @ColumnInfo(name = BaseColumns._ID)
    val id: Long = 0,
    @ColumnInfo(name = "base_url")
    val baseUrl: String,
    @ColumnInfo(name = "backdrop_sizes")
    val backdropSizes: List<String>,
    @ColumnInfo(name = "poster_sizes")
    val posterSizes: List<String>,
    @ColumnInfo(name = "profile_sizes")
    val profileSizes: List<String>
)