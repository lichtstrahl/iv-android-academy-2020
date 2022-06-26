package root.iv.ivandroidacademy.data.cache

abstract class Cache<T>(
    private val supplier: suspend () -> T
) {

    private val cache: T? = null


    suspend fun get(): T = cache ?: supplier.invoke()
}