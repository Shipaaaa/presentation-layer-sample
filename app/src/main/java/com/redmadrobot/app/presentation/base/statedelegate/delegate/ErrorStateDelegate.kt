package com.redmadrobot.app.presentation.base.statedelegate.delegate

import android.view.View
import com.redmadrobot.app.presentation.base.statedelegate.state.ErrorScreenState
import com.redmadrobot.app.presentation.base.statedelegate.state.ErrorScreenState.CONTENT
import com.redmadrobot.app.presentation.base.statedelegate.state.ErrorScreenState.ERROR
import com.redmadrobot.lib.sd.base.State
import com.redmadrobot.lib.sd.base.StateDelegate

class ErrorStateDelegate(
    contentView: List<View>,
    errorView: View
) : StateDelegate<ErrorScreenState>(
    State(CONTENT, contentView),
    State(ERROR, listOf(errorView))
) {

    fun showContent() {
        currentState = CONTENT
    }

    fun showError() {
        currentState = ERROR
    }
}
