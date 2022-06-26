package root.iv.ivandroidacademy.data.model

data class Actor(
    val id: Int,
    val name: String,
    val photoUrl: String?
): DiffUtilModel {
    override fun id(): Long = this.id.toLong()

    override fun same(other: DiffUtilModel): Boolean = this == other

}