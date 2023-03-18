package ru.shipa.app.presentation.base.statedelegate.changestrategy

import com.redmadrobot.lib.sd.base.State
import com.redmadrobot.lib.sd.base.StateChangeStrategy

class NoChangeStrategy<T : Enum<T>> : StateChangeStrategy<T> {

    override fun onStateEnter(state: State<T>, prevState: State<T>?) {
        // Do nothing
    }

    override fun onStateExit(state: State<T>, nextState: State<T>?) {
        // Do nothing
    }
}
