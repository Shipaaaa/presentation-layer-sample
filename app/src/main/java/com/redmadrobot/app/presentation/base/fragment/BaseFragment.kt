package com.redmadrobot.app.presentation.base.fragment

import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.redmadrobot.app.R
import com.redmadrobot.app.extension.hideKeyboard
import com.redmadrobot.app.extension.navigateSafe
import com.redmadrobot.app.presentation.base.activity.BaseActivity
import com.redmadrobot.app.presentation.base.viewmodel.event.*
import com.redmadrobot.app.presentation.utils.getString

abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId), SnackbarHandler {

    private val baseActivity by lazy { activity as BaseActivity }

    override fun onPause() {
        activity?.hideKeyboard()
        super.onPause()
    }

    override fun showMessage(
        messageText: String,
        containerResId: Int,
        anchorViewId: Int?,
        actionTitleId: Int?,
        action: ((View) -> Unit)?,
        duration: Int
    ) {
        baseActivity.showMessage(
            messageText = messageText,
            containerResId = containerResId,
            anchorViewId = anchorViewId,
            actionTitleId = actionTitleId,
            action = action,
            duration = duration
        )
    }

    override fun showError(
        messageText: String,
        containerResId: Int,
        anchorViewId: Int?,
        actionTitleId: Int?,
        action: ((View) -> Unit)?,
        duration: Int
    ) {
        baseActivity.showError(
            messageText = messageText,
            containerResId = containerResId,
            anchorViewId = anchorViewId,
            actionTitleId = actionTitleId,
            action = action,
            duration = duration
        )
    }

    @CallSuper
    protected open fun handleEvents(event: ViewEvent) {
        when (event) {
            is NavigationEvent -> handleNavigationEvent(event)

            is ShowSnackbarMessageEvent -> {
                showMessage(
                    messageText = getString(event.message),
                    containerResId = getMessagesContainer(),
                    anchorViewId = getSnackbarAnchorView()
                )
            }

            is ShowSnackbarErrorEvent -> {
                showError(
                    messageText = getString(event.message),
                    containerResId = getMessagesContainer(),
                    anchorViewId = getSnackbarAnchorView()
                )
            }

            is ShowDialogMessageEvent -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(event.title)
                    .setMessage(event.message)
                    .show()
            }
        }
    }

    private fun handleNavigationEvent(event: NavigationEvent) {
        when (event) {
            is NavigationEvent.ToDirection -> getNavController(event.rootGraph).navigate(event.direction)
            is NavigationEvent.ToRes -> getNavController(event.rootGraph).navigateSafe(event.resId, event.args)
            is NavigationEvent.Up -> findNavController().navigateUp()
            is NavigationEvent.Back -> if (!findNavController().popBackStack()) requireActivity().finish()
            is NavigationEvent.BackTo -> findNavController().popBackStack(event.destinationId, event.inclusive)
        }
    }

    private fun getNavController(rootGraph: Boolean = false): NavController {
        return if (rootGraph) {
            requireActivity().findNavController(R.id.activity_app_container_screens)
        } else {
            findNavController()
        }
    }

}
