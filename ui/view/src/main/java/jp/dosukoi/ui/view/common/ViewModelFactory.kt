package jp.dosukoi.ui.view.common

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.produceViewModels(
    noinline factory: ((SavedStateHandle) -> VM)
): Lazy<VM> = viewModels { ActivitySaveStateViewModelProvider(this, factory) }

class ActivitySaveStateViewModelProvider<VM : ViewModel>(
    activity: ComponentActivity,
    private val factory: (SavedStateHandle) -> VM
) : AbstractSavedStateViewModelFactory(activity, activity.intent?.extras) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T = factory(handle) as T
}
