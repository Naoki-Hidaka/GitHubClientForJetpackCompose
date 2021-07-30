package jp.dosukoi.ui.viewmodel.common

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * LiveData without cache.
 * Support screen regeneration unlike Transformations#distinctUntilChanged.
 */
class NoCacheMutableLiveData<T> : MutableLiveData<T>() {
    private var shouldNotify = AtomicBoolean(false)
    private var observer: Observer<in T>? = null

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        this.observer = observer
        super.observe(owner, ::considerNotify)
    }

    override fun observeForever(observer: Observer<in T>) {
        this.observer = observer
        super.observeForever(::considerNotify)
    }

    override fun setValue(value: T) {
        shouldNotify.set(true)
        super.setValue(value)
    }

    override fun postValue(value: T) {
        shouldNotify.set(true)
        super.postValue(value)
    }

    override fun removeObserver(observer: Observer<in T>) {
        super.removeObserver(observer)
        this.observer = null
    }

    private fun considerNotify(t: T) {
        if (shouldNotify.compareAndSet(true, false)) {
            observer?.onChanged(t)
        }
    }
}