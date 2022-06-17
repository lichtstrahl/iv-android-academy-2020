package root.iv.ivandroidacademy.data.repository

interface Repository<T> {

    suspend fun load(id: Int): T?

    suspend fun loadAll(): List<T>
}