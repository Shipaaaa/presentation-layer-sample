package com.redmadrobot.app.presentation.main

import androidx.hilt.lifecycle.ViewModelInject
import com.redmadrobot.app.extension.log
import com.redmadrobot.app.extension.mapDistinct
import com.redmadrobot.app.infrastructure.SchedulersProvider
import com.redmadrobot.app.infrastructure.scheduleIoToUi
import com.redmadrobot.app.presentation.base.viewmodel.BaseViewModel
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class MainViewModel @ViewModelInject constructor(
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
