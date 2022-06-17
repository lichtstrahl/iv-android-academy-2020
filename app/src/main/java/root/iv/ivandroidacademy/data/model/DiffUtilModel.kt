package root.iv.ivandroidacademy.data.model

/**
 * Каждая модель, используемая в адаптерах должна реализовывать данный интерфейс.
 */
interface DiffUtilModel {

    fun id(): Long

    fun same(other: DiffUtilModel): Boolean
}