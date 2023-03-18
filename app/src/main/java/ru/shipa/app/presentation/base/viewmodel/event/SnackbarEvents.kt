package ru.shipa.app.presentation.base.viewmodel.event

import ru.shipa.app.presentation.utils.StringResource

/**
 * События описывающие отображения Snackbar.
 */

data class ShowSnackbarMessageEvent(val message: StringResource) : ViewEvent

data class ShowSnackbarErrorEvent(val message: StringResource) : ViewEvent
