package jp.dosukoi.ui.view.common

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> ComponentActivity.produceViewModels(
    noinline factory: (() -> T)
): Lazy<T> = viewModels { ViewModelFactory(factory) }

class ViewModelFactory<T : ViewModel>(
    private val factory: (() -> T)
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = factory as T
}