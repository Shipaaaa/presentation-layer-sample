package com.redmadrobot.app.presentation.details

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.redmadrobot.app.data.FilmRepository
import com.redmadrobot.app.domain.model.Film
import com.redmadrobot.app.extension.log
import com.redmadrobot.app.extension.mapDistinct
import com.redmadrobot.app.infrastructure.SchedulersProvider
import com.redmadrobot.app.infrastructure.scheduleIoToUi
import com.redmadrobot.app.presentation.base.viewmodel.BaseViewModel
import com.redmadrobot.app.presentation.base.viewmodel.state.Content
import com.redmadrobot.app.presentation.base.viewmodel.state.Error
import com.redmadrobot.app.presentation.base.viewmodel.state.LceState
import com.redmadrobot.app.presentation.base.viewmodel.state.Loading
import com.redmadrobot.app.presentation.list.FilmsFragmentDirections

class FilmDetailsViewModel @ViewModelInject constructor(
    @Assisted
    private val savedStateHandle: SavedStateHandle,
    private val schedulers: SchedulersProvider,
    private val filmRepository: FilmRepository
) : BaseViewModel<LceState<Film>>() {

    companion object {
        private const val TAG = "FilmDetailsViewModel"
    }

    val details = liveState.mapDistinct { it }

    init {
        // TODO Сделать получение аргумента более безопасным
        val id: Int = requireNotNull(savedStateHandle.get("id"))

        // Читаем данные по неправильному id, чтобы протестировать ошибку.
        loadFilmDetails(-1)
    }

    fun onRetryClick() {
        // TODO Сделать получение аргумента более безопасным
        navigateTo(FilmDetailsFragmentDirections.toList2Fragment())
//        val id: Int = requireNotNull(savedStateHandle.get("id"))
//
//        loadFilmDetails(id)
    }

    private fun loadFilmDetails(id: Int) {
        filmRepository
            .getFilm(id)
            .scheduleIoToUi(schedulers)
            .doOnSubscribe { updateState(Loading()) }
            .safeSubscribe(
                onSuccess = { film -> updateState(Content(film)) },
                onError = { appError ->
                    appError.log(TAG)

                    updateState(Error.fromAppError(appError))
                    showError(appError.message)
                }
            )
    }

}
