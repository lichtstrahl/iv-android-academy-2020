package root.iv.ivandroidacademy.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies_remote_keys")
data class MoviesRemoteKeyEntity(
    @PrimaryKey
    @ColumnInfo(name = "search")
    val search: String,
    @ColumnInfo(name = "next_page")
    val nextPage: Int?,
    @ColumnInfo(name = "prev_page")
    val prevPage: Int?
)
