package ru.shipa.app.presentation.base.statedelegate.changestrategy

import ru.shipa.app.presentation.utils.Animations
import com.redmadrobot.lib.sd.base.ShowOnEnterGoneOnExitStrategy
import com.redmadrobot.lib.sd.base.State

class ShimmersStrategy<T : Enum<T>> : ShowOnEnterGoneOnExitStrategy<T>() {

    private val blinkAnimation = Animations.createBlinkAnimation()
    private val fadeOutAnimation = Animations.createFadeOutAnimation()

    override fun onStateEnter(state: State<T>, prevState: State<T>?) {
        super.onStateEnter(state, prevState)
        state.viewsGroup.forEach { it.startAnimation(blinkAnimation) }
    }

    override fun onStateExit(state: State<T>, nextState: State<T>?) {
        state.viewsGroup.forEach {
            it.clearAnimation()
            if (nextState?.viewsGroup?.contains(it) == false) it.startAnimation(fadeOutAnimation)
        }
        super.onStateExit(state, nextState)
    }
}
