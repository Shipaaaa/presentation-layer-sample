package com.redmadrobot.app.presentation.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.distinctUntilChanged
import com.redmadrobot.app.data.FilmRepository
import com.redmadrobot.app.domain.model.Film
import com.redmadrobot.app.extension.log
import com.redmadrobot.app.infrastructure.SchedulersProvider
import com.redmadrobot.app.infrastructure.scheduleIoToUi
import com.redmadrobot.app.presentation.base.viewmodel.BaseViewModel
import com.redmadrobot.app.presentation.base.viewmodel.state.Content
import com.redmadrobot.app.presentation.base.viewmodel.state.Error
import com.redmadrobot.app.presentation.base.viewmodel.state.Loading

class FilmsViewModel @ViewModelInject constructor(
    private val schedulers: SchedulersProvider,
    private val filmRepository: FilmRepository
) : BaseViewModel<FilmsViewState>(
    FilmsViewState.createInitialState()
) {

    companion object {
        private const val TAG = "FilmsViewModel"
    }

    val filmsViewState = liveState.distinctUntilChanged()

    init {
        loadFavouriteFilms(needShoWLoading = true, forceError = true)
        loadPopularFilms(needShoWLoading = true, forceError = true)
    }

    fun onDetailsClicked(film: Film) {
        navigateTo(FilmsFragmentDirections.toDetailsFragment(film.id))
    }

    fun onRetryClick() {
        loadFavouriteFilms(needShoWLoading = true, forceError = true)
        loadPopularFilms(needShoWLoading = true, forceError = false)
    }

    fun onRefresh() {
        updateState {
            copy(needShowSwipeRefresh = true)
        }
        loadFavouriteFilms(needShoWLoading = false, forceError = false)
        loadPopularFilms(needShoWLoading = false, forceError = false)
    }

    private fun loadFavouriteFilms(needShoWLoading: Boolean, forceError: Boolean) {
        filmRepository
            .getFilms(debugDelay = 2)
            .map { check(!forceError); it }
            .scheduleIoToUi(schedulers)
            .doOnSubscribe {
                if (needShoWLoading) {
                    updateState {
                        copy(favouriteFilmsState = Loading())
                    }
                }
            }
            .safeSubscribe(
                onSuccess = { popularFilms ->
                    updateState {
                        copy(
                            popularFilmsState = Content(popularFilms),
                            needShowSwipeRefresh = false
                        )
                    }
                },
                onError = { appError ->
                    appError.log(TAG)

                    updateState {
                        copy(
                            popularFilmsState = Error.fromAppError(appError),
                            needShowSwipeRefresh = false
                        )
                    }
                    showError(appError.message)
                }
            )
    }

    private fun loadPopularFilms(needShoWLoading: Boolean, forceError: Boolean) {
        filmRepository
            .getFilms(debugDelay = 4)
            .map { check(!forceError); it }
            .scheduleIoToUi(schedulers)
            .doOnSubscribe {
                if (needShoWLoading) {
                    updateState {
                        copy(favouriteFilmsState = Loading())
                    }
                }
            }
            .safeSubscribe(
                onSuccess = { favouriteFilms ->
                    updateState {
                        copy(
                            favouriteFilmsState = Content(favouriteFilms),
                            needShowSwipeRefresh = false
                        )
                    }
                },
                onError = { appError ->
                    appError.log(TAG)

                    updateState {
                        copy(
                            favouriteFilmsState = Error.fromAppError(appError),
                            needShowSwipeRefresh = false
                        )
                    }
                    showError(appError.message)
                }
            )
    }

}
