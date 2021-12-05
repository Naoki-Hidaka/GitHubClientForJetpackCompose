package jp.dosukoi.ui.viewmodel.common

sealed class LoadState<out T> {
    object Loading : LoadState<Nothing>()
    data class Loaded<out T>(val data: T) : LoadState<T>()
    data class Error(val message: String) : LoadState<Nothing>()
}
