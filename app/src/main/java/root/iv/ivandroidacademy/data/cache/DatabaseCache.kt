package root.iv.ivandroidacademy.data.cache

/**
 * Каждый кеш умеет:
 * 1. Выдавать сохранённое значение
 * 2. Обновляться
 */
interface DatabaseCache<T> {

    suspend fun get(): T

    suspend fun refresh(): T
}