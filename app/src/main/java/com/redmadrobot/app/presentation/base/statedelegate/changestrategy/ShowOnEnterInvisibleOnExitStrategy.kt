package com.redmadrobot.app.presentation.base.statedelegate.changestrategy

import com.redmadrobot.app.extension.makeInvisible
import com.redmadrobot.app.extension.makeVisible
import com.redmadrobot.lib.sd.base.State
import com.redmadrobot.lib.sd.base.StateChangeStrategy

open class ShowOnEnterInvisibleOnExitStrategy<T : Enum<T>> : StateChangeStrategy<T> {

    override fun onStateEnter(state: State<T>, prevState: State<T>?) {
        state.viewsGroup.forEach { it.makeVisible() }
    }

    override fun onStateExit(state: State<T>, nextState: State<T>?) {
        state.viewsGroup.forEach { it.makeInvisible() }
    }
}
