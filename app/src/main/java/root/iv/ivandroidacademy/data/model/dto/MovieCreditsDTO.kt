package root.iv.ivandroidacademy.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieCreditsDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("cast")
    val cast: List<ActorDTO>
)
