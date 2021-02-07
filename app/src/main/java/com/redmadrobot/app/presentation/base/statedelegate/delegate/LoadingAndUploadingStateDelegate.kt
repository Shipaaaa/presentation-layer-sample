package com.redmadrobot.app.presentation.base.statedelegate.delegate

import android.view.View
import com.redmadrobot.app.presentation.base.statedelegate.changestrategy.ProgressStrategy
import com.redmadrobot.app.presentation.base.statedelegate.changestrategy.ShimmersStrategy
import com.redmadrobot.app.presentation.base.statedelegate.state.LoadingAndUploadingScreenState
import com.redmadrobot.app.presentation.base.statedelegate.state.LoadingAndUploadingScreenState.*
import com.redmadrobot.lib.sd.base.State
import com.redmadrobot.lib.sd.base.StateChangeStrategy
import com.redmadrobot.lib.sd.base.StateDelegate

class LoadingAndUploadingStateDelegate(
    shimmerView: List<View>,
    shimmerStateChangeStrategy: StateChangeStrategy<LoadingAndUploadingScreenState> = ShimmersStrategy(),
    contentViews: List<View>,
    progressViews: List<View>,
    progressStateChangeStrategy: StateChangeStrategy<LoadingAndUploadingScreenState> = ProgressStrategy(progressViews.last()),
    errorViews: List<View>
) : StateDelegate<LoadingAndUploadingScreenState>(
    State(SHIMMER, shimmerView, shimmerStateChangeStrategy),
    State(CONTENT, contentViews),
    State(PROGRESS, contentViews + progressViews, progressStateChangeStrategy),
    State(ERROR, errorViews)
) {

    constructor(
        shimmerView: View,
        shimmerStateChangeStrategy: StateChangeStrategy<LoadingAndUploadingScreenState> = ShimmersStrategy(),
        contentView: View,
        progressView: View,
        progressStateChangeStrategy: StateChangeStrategy<LoadingAndUploadingScreenState> = ProgressStrategy(progressView),
        errorView: View
    ) : this(
        listOf(shimmerView),
        shimmerStateChangeStrategy,
        listOf(contentView),
        listOf(progressView),
        progressStateChangeStrategy,
        listOf(errorView)
    )

    fun showShimmer() {
        currentState = SHIMMER
    }

    fun showContent() {
        currentState = CONTENT
    }

    fun showProgress() {
        currentState = PROGRESS
    }

    fun showError() {
        currentState = ERROR
    }
}
