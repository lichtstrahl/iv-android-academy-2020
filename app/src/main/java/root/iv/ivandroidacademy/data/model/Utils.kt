package root.iv.ivandroidacademy.data.model

fun String.toImageUrl(base: String, size: String) = "$base$size$this"

fun List<String>.small() = this.first()

fun List<String>.middle() = this[this.size/2]

fun List<String>.extra() = this.last()