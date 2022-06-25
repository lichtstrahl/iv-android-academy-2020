package root.iv.ivandroidacademy.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenresContainer(
    @SerialName("genres")
    val genres: List<GenreDTO>
)
