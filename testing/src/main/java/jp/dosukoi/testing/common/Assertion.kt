package jp.dosukoi.testing.common

import com.google.common.truth.Truth

inline fun <reified T : Any> assertType(target: Any?, block: T.() -> Unit) {
    if (target is T) {
        target.block()
    } else {
        Truth.assertThat(target).isInstanceOf(T::class.java)
    }
}
