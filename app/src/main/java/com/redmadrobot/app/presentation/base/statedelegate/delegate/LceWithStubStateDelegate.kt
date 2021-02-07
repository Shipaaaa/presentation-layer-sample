package com.redmadrobot.app.presentation.base.statedelegate.delegate

import android.view.View
import com.redmadrobot.app.presentation.base.statedelegate.changestrategy.FadeStrategy
import com.redmadrobot.app.presentation.base.statedelegate.changestrategy.ProgressStrategy
import com.redmadrobot.app.presentation.base.statedelegate.state.LceWithStubScreenState
import com.redmadrobot.app.presentation.base.statedelegate.state.LceWithStubScreenState.*
import com.redmadrobot.lib.sd.base.State
import com.redmadrobot.lib.sd.base.StateChangeStrategy
import com.redmadrobot.lib.sd.base.StateDelegate

class LceWithStubStateDelegate(
    loadingViews: List<View>,
    loadingStateChangeStrategy: StateChangeStrategy<LceWithStubScreenState> = ProgressStrategy(loadingViews.last()),
    contentViews: List<View>,
    stubViews: List<View>,
    errorViews: List<View>
) : StateDelegate<LceWithStubScreenState>(
    State(LOADING, loadingViews, loadingStateChangeStrategy),
    State(CONTENT, contentViews, FadeStrategy()),
    State(STUB, stubViews),
    State(ERROR, errorViews)
) {

    constructor(
        loadingView: View,
        loadingStateChangeStrategy: StateChangeStrategy<LceWithStubScreenState> = ProgressStrategy(loadingView),
        contentView: View,
        stubView: View,
        errorView: View
    ) : this(
        listOf(loadingView),
        loadingStateChangeStrategy,
        listOf(contentView),
        listOf(stubView),
        listOf(errorView)
    )

    fun showLoading() {
        currentState = LOADING
    }

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
