package ru.shipa.app.presentation.details

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.shipa.app.data.FilmRepository
import ru.shipa.app.domain.model.Film
import ru.shipa.app.extension.log
import ru.shipa.app.extension.mapDistinct
import ru.shipa.app.infrastructure.SchedulersProvider
import ru.shipa.app.infrastructure.scheduleIoToUi
import ru.shipa.app.presentation.base.viewmodel.BaseViewModel
import ru.shipa.app.presentation.base.viewmodel.state.Content
import ru.shipa.app.presentation.base.viewmodel.state.Error
import ru.shipa.app.presentation.base.viewmodel.state.LceState
import ru.shipa.app.presentation.base.viewmodel.state.Loading

class FilmDetailsViewModel @AssistedInject constructor(
    @Assisted
    private val filmId: Int,
    private val schedulers: SchedulersProvider,
    private val filmRepository: FilmRepository
) : BaseViewModel<LceState<Film>>() {

    companion object {
        private const val TAG = "FilmDetailsViewModel"
    }

    val details = liveState.mapDistinct { it }

    init {
        filmId
        // Читаем данные по-неправильному id, чтобы протестировать ошибку.
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


    @AssistedFactory
    interface Factory {
        fun create(filmId: Int): FilmDetailsViewModel
    }
}
