package com.redmadrobot.app.presentation.main

import com.redmadrobot.app.presentation.base.viewmodel.state.BaseState

data class MainViewState(
    val login: String,
    val password: String,
    val buttonEnabled: Boolean,
    val needShowProgress: Boolean
) : BaseState {

    companion object {
        fun createInitialState(): MainViewState {
            return MainViewState(
                login = "",
                password = "",
                buttonEnabled = false,
                needShowProgress = false
            )
        }
    }

}
