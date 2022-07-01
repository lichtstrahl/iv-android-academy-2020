package root.iv.ivandroidacademy.data.database.entity

import android.provider.BaseColumns
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "tags")
    val tags: String,
    @ColumnInfo(name = "duration")
    val duration: Int?,
    @ColumnInfo(name = "age_limit")
    val ageLimit: Int,
    @ColumnInfo(name = "rating")
    val rating: Double,
    @ColumnInfo(name = "reviews_count")
    val reviewsCount: Int,
    @ColumnInfo(name = "poster_url")
    val posterUrl: String,
    @ColumnInfo(name = "backdrop_url")
    val backdropUrl: String,
    @ColumnInfo(name = "is_like")
    val isLike: Boolean,
    @ColumnInfo(name = "storyline")
    val storyline: String
)
