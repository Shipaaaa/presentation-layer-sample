package ru.shipa.app.presentation.main

import ru.shipa.app.presentation.base.viewmodel.state.BaseState

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
