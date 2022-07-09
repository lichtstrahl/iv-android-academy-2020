package root.iv.ivandroidacademy.viewmodel.util

sealed class DataState<out T> {

    data class Loading(val progress: Int?): DataState<Nothing>()
    data class Success<T>(val data: T): DataState<T>()
    data class Error(val t: Throwable): DataState<Nothing>()
}