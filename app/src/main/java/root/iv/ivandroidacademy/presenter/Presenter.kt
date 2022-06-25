package root.iv.ivandroidacademy.presenter

interface Presenter<V> {

    fun attach(view: V)

    /**
     * Отсоединение View без прекращения фоновой работы
     */
    fun detach()

    /**
     * Callback, после вызова которого данные больше не понадобятся.
     * Прекращение фоновой работы.
     */
    fun cancel()
}