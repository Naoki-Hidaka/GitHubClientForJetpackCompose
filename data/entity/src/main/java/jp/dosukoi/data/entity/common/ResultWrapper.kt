package jp.dosukoi.data.entity.common

sealed class ResultWrapper<out T> {
    class Success<out T>(val data: T) : ResultWrapper<T>()
    class GenericError(val code: Int? = null, val error: ErrorResponse? = null) :
        ResultWrapper<Nothing>()

    object NetworkError : ResultWrapper<Nothing>()
}