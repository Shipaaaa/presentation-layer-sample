package ru.shipa.app.presentation.base.statedelegate.delegate

import android.view.View
import ru.shipa.app.presentation.base.statedelegate.changestrategy.FadeStrategy
import ru.shipa.app.presentation.base.statedelegate.state.ErrorWithStubScreenState
import ru.shipa.app.presentation.base.statedelegate.state.ErrorWithStubScreenState.*
import com.redmadrobot.lib.sd.base.State
import com.redmadrobot.lib.sd.base.StateDelegate

class ErrorWithStubStateDelegate(
    contentViews: List<View>,
    stubViews: List<View>,
    errorViews: List<View>
) : StateDelegate<ErrorWithStubScreenState>(
    State(CONTENT, contentViews, FadeStrategy()),
    State(STUB, stubViews),
    State(ERROR, errorViews)
) {

    constructor(
        contentView: View,
        stubView: View,
        errorView: View
    ) : this(
        listOf(contentView),
        listOf(stubView),
        listOf(errorView)
    )

    fun showContent() {
        currentState = CONTENT
    }

    fun showStub() {
        currentState = STUB
    }

    fun showError() {
        currentState = ERROR
    }

}
