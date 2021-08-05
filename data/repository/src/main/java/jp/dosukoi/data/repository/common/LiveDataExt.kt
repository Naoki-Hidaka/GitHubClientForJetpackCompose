package jp.dosukoi.data.repository.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T, R, C> zip(
    liveData1: LiveData<T>,
    liveData2: LiveData<R>,
    initialValue: C? = null,
    operator: (T, R) -> C
): LiveData<C> {
    return MediatorLiveData<C>().apply {
        value = initialValue
        listOf(
            liveData1,
            liveData2
        ).forEach { liveData ->
            addSource(liveData) {
                val liveData1Value = liveData1.value
                val liveData2Value = liveData2.value
                if (liveData1Value != null && liveData2Value != null) {
                    value = operator(liveData1Value, liveData2Value)
                }
            }
        }
    }
}