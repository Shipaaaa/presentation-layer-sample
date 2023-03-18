package ru.shipa.app.presentation.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.shipa.app.extension.delegate
import ru.shipa.app.extension.onNext
import ru.shipa.app.presentation.base.viewmodel.event.EventsQueue
import ru.shipa.app.presentation.base.viewmodel.event.ViewEvent
import ru.shipa.app.presentation.base.viewmodel.state.BaseState

abstract class BaseViewModel<T : BaseState>(
    initialState: T? = null
) : ViewModel(),
    EventsDispatcher,
    SafelySubscribable {

    protected val liveState = MutableLiveData<T>().apply {
        initialState?.let { onNext(it) }
    }

    protected var state: T by liveState.delegate()

    override val events = EventsQueue<ViewEvent>()

    override val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    /**
     * Метод для установки состояния экрана.
     * Позволяет отследить все обновления состояния на сложных экранах.
     *
     * Пример:
     * ```
     *  updateState(Loading())
     * ```
     */
    protected fun updateState(newState: T) {
        state = newState
    }

    /**
     * Метод для изменения состояния экрана.
     * Позволяет отследить все обновления состояния на сложных экранах.
     *
     * Пример:
     * ```
     *  updateState {
     *      copy(
     *          favouriteFilmsState = Content(favouriteFilms),
     *          needShowSwipeRefresh = false
     *      )
     *  }
     * ```
     */
    protected fun updateState(block: T.() -> T) {
        state = block.invoke(state)
    }

}
