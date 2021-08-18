package jp.dosukoi.ui.viewmodel.common

sealed class LoadState<out T> {
    object Loading : LoadState<Nothing>()
    data class Loaded<out T>(val value: T) : LoadState<T>()
    object Error : LoadState<Nothing>()
}
