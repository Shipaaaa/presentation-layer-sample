package ru.shipa.app.presentation.base.statedelegate.delegate

import android.view.View
import ru.shipa.app.presentation.base.statedelegate.changestrategy.NoChangeStrategy
import ru.shipa.app.presentation.base.statedelegate.changestrategy.ProgressStrategy
import ru.shipa.app.presentation.base.statedelegate.state.UploadingScreenState
import ru.shipa.app.presentation.base.statedelegate.state.UploadingScreenState.CONTENT
import ru.shipa.app.presentation.base.statedelegate.state.UploadingScreenState.LOADING
import com.redmadrobot.lib.sd.base.State
import com.redmadrobot.lib.sd.base.StateDelegate

class UploadingStateDelegate(
    progressView: View
) : StateDelegate<UploadingScreenState>(
    State(LOADING, listOf(progressView), ProgressStrategy(progressView)),
    State(CONTENT, emptyList(), NoChangeStrategy())
) {
    fun showContent() {
        currentState = CONTENT
    }

    fun showLoading() {
        currentState = LOADING
    }
}
