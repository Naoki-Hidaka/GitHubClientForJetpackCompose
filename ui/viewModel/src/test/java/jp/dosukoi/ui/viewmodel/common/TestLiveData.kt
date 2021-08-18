package jp.dosukoi.ui.viewmodel.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class TestLiveData<T>(private val liveData: LiveData<T>) {
    private val values = mutableListOf<T>()
    private val observer = Observer<T> { values.add(it) }

    init {
        liveData.observeForever(observer)
    }

    fun values(): MutableList<T> {
        shutdown()
        return values
    }

    fun lastValue(): T {
        shutdown()
        return values.last()
    }

    fun complete() = shutdown()

    private fun shutdown() {
        if (liveData.hasActiveObservers()) {
            liveData.removeObserver(observer)
        }
    }
}

fun <T> LiveData<T>.test() = TestLiveData(this)
