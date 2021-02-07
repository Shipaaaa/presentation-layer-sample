package com.redmadrobot.app.extension

import timber.log.Timber

fun ApplicationError.log(tag: String = "NO_TAG") {
    Timber.tag(tag).e(throwable, "Error message: ${message.get()}\n")
}

fun Any.log(tag: String = "NO_TAG", prefix: String = javaClass.name) {
    Timber.tag(tag).d("$prefix ${toString()}")
}
