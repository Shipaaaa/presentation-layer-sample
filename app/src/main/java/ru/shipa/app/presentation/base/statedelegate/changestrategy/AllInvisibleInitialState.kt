package ru.shipa.app.presentation.base.statedelegate.changestrategy

import ru.shipa.app.extension.makeInvisible
import com.redmadrobot.lib.sd.base.InitialState
import com.redmadrobot.lib.sd.base.State

class AllInvisibleInitialState<T : Enum<T>> : InitialState<T> {

    override fun apply(states: Array<out State<T>>) {
        states.forEach { state -> state.viewsGroup.forEach { it.makeInvisible() } }
    }
}
