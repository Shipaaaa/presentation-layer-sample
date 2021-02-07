package com.redmadrobot.app.presentation.base.viewmodel

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavDirections
import com.redmadrobot.app.presentation.base.viewmodel.event.*
import com.redmadrobot.app.presentation.utils.StringResource

interface EventsDispatcher {

    val events: EventsQueue<ViewEvent>

    fun showMessage(message: StringResource) {
        events.onNext(ShowSnackbarMessageEvent(message))
    }

    fun showError(message: StringResource) {
        events.onNext(ShowSnackbarErrorEvent(message))
    }

    fun navigateTo(direction: NavDirections, rootGraph: Boolean = false) {
        events.onNext(NavigationEvent.ToDirection(direction, rootGraph))
    }

    fun navigateTo(@IdRes resId: Int, args: Bundle? = null, rootGraph: Boolean = false) {
        events.onNext(NavigationEvent.ToRes(resId, args, rootGraph))
    }

    fun navigateUp() {
        events.onNext(NavigationEvent.Up())
    }

    fun navigateBack() {
        events.onNext(NavigationEvent.Back())
    }

}
