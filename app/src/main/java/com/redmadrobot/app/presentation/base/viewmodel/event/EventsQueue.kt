package com.redmadrobot.app.presentation.base.viewmodel.event

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.*

/**
 * Реализация LiveData, которая выполняет события один раз.
 */
class EventsQueue<T> : MutableLiveData<Queue<T>>() {

    fun onNext(value: T) {
        val events = getValue() ?: LinkedList()
        events.add(value)
        setValue(events)
    }
}

inline fun <T : Any> LifecycleOwner.observe(liveData: EventsQueue<T>, crossinline block: (T) -> Unit) {
    liveData.observe(this, Observer { events ->
        val iterator = events.iterator()
        while (iterator.hasNext()) {
            block.invoke(iterator.next())
            iterator.remove()
        }
    })
}

