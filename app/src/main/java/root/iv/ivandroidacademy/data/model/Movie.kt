package root.iv.ivandroidacademy.data.model

data class Movie(
    val id: Int = 0,
    val title: String = "",
    val tags: String = "",
    val duration: Int? = 0,
    val ageLimit: Int = 0,
    val rating: Float = 0f,
    val reviewsCount: Int = 0,
    val poster: String = "",
    val poster2: String = "",
    val isLike: Boolean = false,
    val storyline: String = "",
): DiffUtilModel {

    override fun id(): Long = this.id.toLong()

    override fun same(other: DiffUtilModel): Boolean = this == other
}
