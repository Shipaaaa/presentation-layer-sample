package ru.shipa.app.presentation.base.statedelegate.changestrategy

import android.view.View
import com.redmadrobot.lib.sd.base.ShowOnEnterGoneOnExitStrategy
import com.redmadrobot.lib.sd.base.State

// TODO Добавить включение анимации
class ProgressStrategy<T : Enum<T>>(private val progressContainer: View) : ShowOnEnterGoneOnExitStrategy<T>() {

    override fun onStateEnter(state: State<T>, prevState: State<T>?) {
        super.onStateEnter(state, prevState)
//        progressContainer.progress
    }

    override fun onStateExit(state: State<T>, nextState: State<T>?) {
//        progressContainer.progress
        super.onStateExit(state, nextState)
    }
}
