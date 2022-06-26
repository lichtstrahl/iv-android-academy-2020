package root.iv.ivandroidacademy.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageConfigurationDTO(
    @SerialName("images")
    val config: Config
) {
    @Serializable
    data class Config(
        @SerialName("base_url")
        val baseUrl: String,
        @SerialName("backdrop_sizes")
        val backdropSizes: List<String>,
        @SerialName("poster_sizes")
        val posterSizes: List<String>,
        @SerialName("profile_sizes")
        val profileSizes: List<String>
    )
}
