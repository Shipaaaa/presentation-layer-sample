package com.redmadrobot.app.presentation.base.viewmodel.event

/**
 * События описывающие взаимодействия с системой и другими приложениями.
 */

data class OpenLinkEvent(val url: String) : ViewEvent

data class OpenMapAppEvent(val latitude: Double, val longitude: Double) : ViewEvent

data class OpenCallAppEvent(val phoneNumber: String) : ViewEvent

data class OpenEmailAppEvent(
    val titleForChooser: String,
    val email: String,
    val subject: String = "",
    val body: String = ""
) : ViewEvent

class OpenPlayStoreAppEvent : ViewEvent

class OpenContactPickerEvent : ViewEvent

data class ShareTextEvent(val text: String) : ViewEvent

class OpenAppSettingsEvent : ViewEvent
