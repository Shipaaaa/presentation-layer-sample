package com.redmadrobot.app.presentation.list

import com.redmadrobot.app.domain.model.Film
import com.redmadrobot.app.presentation.base.viewmodel.state.BaseState
import com.redmadrobot.app.presentation.base.viewmodel.state.LceState
import com.redmadrobot.app.presentation.base.viewmodel.state.Loading

data class FilmsViewState(
    val favouriteFilmsState: LceState<List<Film>>,
    val popularFilmsState: LceState<List<Film>>,
    val needShowSwipeRefresh: Boolean
) : BaseState {

    companion object {
        fun createInitialState(): FilmsViewState {
            return FilmsViewState(
                favouriteFilmsState = Loading(),
                popularFilmsState = Loading(),
                needShowSwipeRefresh = false
            )
        }
    }

}
