package jp.dosukoi.data.repository.common

import retrofit2.HttpException
import retrofit2.Response

suspend fun <T> asyncFetch(
    apiFunction: suspend () -> Response<T>,
    onSuccess: suspend (T) -> Unit
) {
    try {
        val response = apiFunction.invoke()
        if (response.isSuccessful) {
            onSuccess(response.body()!!)
        } else throw HttpException(response)
    } catch (throwable: Throwable) {
        throw throwable
    }
}

suspend fun <T> asyncFetch(
    apiFunction: suspend () -> Response<T>,
): T {
    return try {
        val response = apiFunction.invoke()
        if (response.isSuccessful) {
            response.body()!!
        } else throw HttpException(response)
    } catch (throwable: Throwable) {
        throw throwable
    }
}
