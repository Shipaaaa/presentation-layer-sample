package ru.shipa.app.presentation.list

import androidx.lifecycle.distinctUntilChanged
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.shipa.app.data.FilmRepository
import ru.shipa.app.domain.model.Film
import ru.shipa.app.extension.log
import ru.shipa.app.infrastructure.SchedulersProvider
import ru.shipa.app.infrastructure.scheduleIoToUi
import ru.shipa.app.presentation.base.viewmodel.BaseViewModel
import ru.shipa.app.presentation.base.viewmodel.state.Content
import ru.shipa.app.presentation.base.viewmodel.state.Error
import ru.shipa.app.presentation.base.viewmodel.state.Loading
import javax.inject.Inject

@HiltViewModel
class FilmsViewModel @Inject constructor(
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
