package com.redmadrobot.app.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <T> LifecycleOwner.observe(liveData: LiveData<T>, crossinline block: (T) -> Unit) {
    liveData.observe(this, Observer { block.invoke(it) })
}
