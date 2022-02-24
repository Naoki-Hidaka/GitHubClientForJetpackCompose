package jp.dosukoi.ui.view.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import jp.dosukoi.ui.view.R
import retrofit2.HttpException
import java.io.IOException

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.showToast(@StringRes messageResId: Int) {
    val message = getString(messageResId)

    showToast(message)
}

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

fun Activity.navigateChrome(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
}
