package root.iv.ivandroidacademy.data.model

data class Movie(
    val id: Int = 0,
    val title: String = "",
    val tags: String = "",
    val duration: Int = 0,
    val ageLimit: Int = 0,
    val rating: Float = 0f,
    val reviewsCount: Int = 0,
    val poster: String = "",
    val poster2: String = "",
    val isLike: Boolean = false,
    val storyline: String = "",
    val actorIds: List<Int> = listOf(),
)
