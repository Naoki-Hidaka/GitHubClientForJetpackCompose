package jp.dosukoi.ui.view.common

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import jp.dosukoi.ui.viewmodel.common.LoadState

@BindingAdapter("visible")
fun View.setVisible(visible: Boolean?) {
    visible ?: return
    isVisible = visible
}

@BindingAdapter("loadVisible")
fun <T> View.setLoadStateVisible(loadState: LoadState<T>?) {
    loadState ?: return
    isVisible = when (loadState) {
        LoadState.Loading -> true
        else -> false
    }
}
