package root.iv.ivandroidacademy.data.model

data class Movie(
    val id: Int,
    val title: String,
    val tags: String,
    val duration: Int?,
    val ageLimit: Int,
    val rating: Float,
    val reviewsCount: Int,
    val poster: String?,
    val poster2: String?,
    val isLike: Boolean,
    val storyline: String,
): DiffUtilModel {

    override fun id(): Long = this.id.toLong()

    override fun same(other: DiffUtilModel): Boolean = this == other
}
