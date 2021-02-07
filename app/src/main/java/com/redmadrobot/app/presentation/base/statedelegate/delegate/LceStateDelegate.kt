package com.redmadrobot.app.presentation.base.statedelegate.delegate

import android.view.View
import com.redmadrobot.app.presentation.base.statedelegate.changestrategy.FadeStrategy
import com.redmadrobot.app.presentation.base.statedelegate.changestrategy.ProgressStrategy
import com.redmadrobot.app.presentation.base.statedelegate.state.LceScreenState
import com.redmadrobot.app.presentation.base.statedelegate.state.LceScreenState.*
import com.redmadrobot.lib.sd.base.State
import com.redmadrobot.lib.sd.base.StateChangeStrategy
import com.redmadrobot.lib.sd.base.StateDelegate

class LceStateDelegate(
    loadingViews: List<View>,
    loadingStateChangeStrategy: StateChangeStrategy<LceScreenState> = ProgressStrategy(loadingViews.last()),
    contentViews: List<View>,
    errorViews: List<View>
) : StateDelegate<LceScreenState>(
    State(LOADING, loadingViews, loadingStateChangeStrategy),
    State(CONTENT, contentViews, FadeStrategy()),
    State(ERROR, errorViews)
) {

    constructor(
        loadingView: View,
        loadingStateChangeStrategy: StateChangeStrategy<LceScreenState> = ProgressStrategy(loadingView),
        contentView: View,
        errorView: View
    ) : this(
        listOf(loadingView),
        loadingStateChangeStrategy,
        listOf(contentView),
        listOf(errorView)
    )

    fun showLoading() {
        currentState = LOADING
    }

    fun showContent() {
        currentState = CONTENT
    }

    fun showError() {
        currentState = ERROR
    }
}
