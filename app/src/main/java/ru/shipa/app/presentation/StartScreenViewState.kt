package ru.shipa.app.presentation

import android.os.Bundle
import androidx.annotation.IdRes
import ru.shipa.app.presentation.base.viewmodel.state.BaseState

data class StartScreenViewState(
    @IdRes val resId: Int,
    val args: Bundle? = null
) : BaseState
