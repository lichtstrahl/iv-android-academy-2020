package root.iv.ivandroidacademy.data.model

data class Movie(
    val id: Int = 0,
    val title: String = "",
    val tags: String = "",
    val duration: Int = 0,
    val pg: String = "6+",
    val rating: Float = 0f,
    val reviewsCount: Int = 0,
    val poster: String = "",
    val poster2: String = "",
    val isLike: Boolean = false,
    val storyLine: String = "",
    val castIds: List<Int> = listOf(),
)
