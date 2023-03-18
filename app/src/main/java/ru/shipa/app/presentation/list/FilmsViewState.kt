package ru.shipa.app.presentation.list

import ru.shipa.app.domain.model.Film
import ru.shipa.app.presentation.base.viewmodel.state.BaseState
import ru.shipa.app.presentation.base.viewmodel.state.LceState
import ru.shipa.app.presentation.base.viewmodel.state.Loading

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
