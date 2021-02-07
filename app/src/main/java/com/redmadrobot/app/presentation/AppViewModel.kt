package com.redmadrobot.app.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.distinctUntilChanged
import com.redmadrobot.app.R
import com.redmadrobot.app.presentation.base.viewmodel.BaseViewModel

class AppViewModel @ViewModelInject constructor() : BaseViewModel<StartScreenViewState>() {

    val startScreen = liveState.distinctUntilChanged()

    init {
        updateState(StartScreenViewState(R.id.mainFragment))
    }

}
