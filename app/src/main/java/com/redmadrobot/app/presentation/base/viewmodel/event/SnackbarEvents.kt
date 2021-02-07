package com.redmadrobot.app.presentation.base.viewmodel.event

import com.redmadrobot.app.presentation.utils.StringResource

/**
 * События описывающие отображения Snackbar.
 */

data class ShowSnackbarMessageEvent(val message: StringResource) : ViewEvent

data class ShowSnackbarErrorEvent(val message: StringResource) : ViewEvent
