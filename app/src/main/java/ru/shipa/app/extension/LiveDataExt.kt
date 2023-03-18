package ru.shipa.app.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Sequential call of [map] and [distinctUntilChanged] in one function.
 * Requires `androidx.lifecycle:lifecycle-livedata-ktx` dependency
 */
inline fun <X, Y> LiveData<X>.mapDistinct(crossinline transform: (X) -> Y): LiveData<Y> {
    return map(transform).distinctUntilChanged()
}

/**
 * Delegate for easier work with [MutableLiveData] content.
 *
 * It is shortest version of:
 * ```
 *  val liveState = MutableLiveData<IntroViewState>(initialState)
 *  var state: IntroViewState
 *      get() = liveState.requireValue()
 *      set(value) = liveState.onNext(value)
 * ```
 * With delegate you can replace it with next code:
 * ```
 *  val liveState = MutableLiveData<IntroViewState>(initialState)
 *  var state: IntroViewState by liveState.delegate()
 * ```
 */
fun <T : Any> MutableLiveData<T>.delegate(): ReadWriteProperty<Any, T> {
    return object : ReadWriteProperty<Any, T> {
        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = onNext(value)
        override fun getValue(thisRef: Any, property: KProperty<*>): T = requireValue()
    }
}

fun <T> MutableLiveData<T>.onNext(next: T) {
    this.value = next
}

fun <T : Any> LiveData<T>.requireValue(): T = checkNotNull(value)
