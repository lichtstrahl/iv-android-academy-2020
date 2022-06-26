package root.iv.ivandroidacademy.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("gender")
    val gender: Int?,
    @SerialName("name")
    val name: String,
    @SerialName("profile_path")
    val photoUrl: String?
)
