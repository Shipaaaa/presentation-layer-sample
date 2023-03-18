package ru.shipa.app.presentation

import androidx.lifecycle.distinctUntilChanged
import ru.shipa.app.R
import ru.shipa.app.presentation.base.viewmodel.BaseViewModel
import javax.inject.Inject

class AppViewModel @Inject constructor() : BaseViewModel<StartScreenViewState>() {

    val startScreen = liveState.distinctUntilChanged()

    init {
        updateState(StartScreenViewState(R.id.mainFragment))
    }

}
