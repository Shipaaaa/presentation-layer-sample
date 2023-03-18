package ru.shipa.app.presentation.base.statedelegate.changestrategy

import ru.shipa.app.extension.makeInvisible
import ru.shipa.app.extension.makeVisible
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
