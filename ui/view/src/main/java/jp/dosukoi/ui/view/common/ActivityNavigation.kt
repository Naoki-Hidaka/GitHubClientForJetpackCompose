package jp.dosukoi.ui.view.common

import android.content.Context
import android.widget.Toast
import jp.dosukoi.ui.view.R
import retrofit2.HttpException
import java.io.IOException

fun Context.showErrorToast(throwable: Throwable) {
    val message = when (throwable) {
        is IOException -> getString(R.string.io_exception)
        is HttpException -> when (throwable.code()) {
            401, 403 -> getString(R.string.authorize_exception)
            else -> getString(R.string.general_exception)
        }
        else -> getString(R.string.general_exception)
    }
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
