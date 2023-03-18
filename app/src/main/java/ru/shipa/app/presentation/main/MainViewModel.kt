package ru.shipa.app.presentation.main

import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.Observable
import ru.shipa.app.extension.log
import ru.shipa.app.extension.mapDistinct
import ru.shipa.app.infrastructure.SchedulersProvider
import ru.shipa.app.infrastructure.scheduleIoToUi
import ru.shipa.app.presentation.base.viewmodel.BaseViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val schedulers: SchedulersProvider
) : BaseViewModel<MainViewState>(
    MainViewState.createInitialState()
) {

    companion object {
        private const val TAG = "MainViewModel"
    }

    val login = liveState.mapDistinct { it.login }
    val password = liveState.mapDistinct { it.password }
    val buttonEnabled = liveState.mapDistinct { it.buttonEnabled }
    val needShowProgress = liveState.mapDistinct { it.needShowProgress }

    fun onCredentialsChanged(login: String, password: String) {
        val buttonEnabled = login.isNotBlank() && password.isNotBlank()

        updateState {
            copy(
                login = login,
                password = password,
                buttonEnabled = buttonEnabled
            )
        }
    }

    fun onEnterButtonClick(login: String, password: String) {
        // Примитивная проверка для тестирования смены состояний.
        Observable
            .timer(3, TimeUnit.SECONDS)
            .flatMapCompletable {
                Completable.fromCallable { check(login == password) }
            }
            .scheduleIoToUi(schedulers)
            .doOnSubscribe { updateState { copy(needShowProgress = true) } }
            .safeSubscribe(
                onComplete = {
                    updateState { copy(needShowProgress = false) }
                    navigateTo(MainFragmentDirections.toListFragment())
                },
                onError = { appError ->
                    appError.log(TAG)

                    updateState { copy(needShowProgress = false) }
                    showError(appError.message)
                }
            )
    }

}
