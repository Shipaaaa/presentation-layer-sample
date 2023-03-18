package ru.shipa.app.presentation.base.viewmodel.event

import android.os.Bundle

/**
 * События описывающие отображения диалогов.
 */

data class ShowDialogMessageEvent(
    val message: String = "",
    val title: String = ""
) : ViewEvent

data class ShowCallPermissionDialogEvent(
    val shouldShowSettingsButton: Boolean,
    val useClosingCallback: Boolean = false
) : ViewEvent

class ShowCameraPermissionDialogEvent : ViewEvent

class ShowRedirectTurnOffConfirmDialogEvent : ViewEvent

class ShowPhoneRedirectInfoDialogEvent : ViewEvent

class FirstConfirmRemoveKeyDialogEvent : ViewEvent
class SecondConfirmRemoveKeyDialogEvent : ViewEvent

class ShowLogoutConfirmDialogEvent : ViewEvent

class ShowEmergencyCodeDialogEvent(val args: Bundle) : ViewEvent
