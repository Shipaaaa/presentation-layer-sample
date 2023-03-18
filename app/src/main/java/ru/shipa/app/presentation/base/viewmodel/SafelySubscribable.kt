package ru.shipa.app.presentation.base.viewmodel

import ru.shipa.app.extension.ApplicationError
import ru.shipa.app.extension.smartSubscribe
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface SafelySubscribable {

    val compositeDisposable: CompositeDisposable

    fun <T> Single<T>.safeSubscribe(
        onSuccess: (T) -> Unit,
        onNetworkConnectionError: ((Throwable) -> Unit)? = null,
        onServerError: ((ru.shipa.app.data.exception.ServerException) -> Unit)? = null,
        onError: ((ApplicationError) -> Unit)? = null
    ): Disposable {

        return smartSubscribe(
            onSuccess,
            onNetworkConnectionError,
            onServerError,
            onError
        ).apply { compositeDisposable.add(this) }
    }

    fun <T> Observable<T>.safeSubscribe(
        onSuccess: (T) -> Unit,
        onComplete: (() -> Unit)? = null,
        onNetworkConnectionError: ((Throwable) -> Unit)? = null,
        onServerError: ((ru.shipa.app.data.exception.ServerException) -> Unit)? = null,
        onError: ((ApplicationError) -> Unit)? = null
    ): Disposable {

        return smartSubscribe(
            onSuccess,
            onComplete,
            onNetworkConnectionError,
            onServerError,
            onError
        ).apply { compositeDisposable.add(this) }
    }

    fun <T> Flowable<T>.safeSubscribe(
        onSuccess: (T) -> Unit,
        onNetworkConnectionError: ((Throwable) -> Unit)? = null,
        onServerError: ((ru.shipa.app.data.exception.ServerException) -> Unit)? = null,
        onError: ((ApplicationError) -> Unit)? = null
    ): Disposable {

        return smartSubscribe(
            onSuccess,
            onNetworkConnectionError,
            onServerError,
            onError
        ).apply { compositeDisposable.add(this) }
    }

    fun Completable.safeSubscribe(
        onComplete: (() -> Unit)? = null,
        onNetworkConnectionError: ((Throwable) -> Unit)? = null,
        onServerError: ((ru.shipa.app.data.exception.ServerException) -> Unit)? = null,
        onError: ((ApplicationError) -> Unit)? = null
    ): Disposable {

        return smartSubscribe(
            onComplete,
            onNetworkConnectionError,
            onServerError,
            onError
        ).apply { compositeDisposable.add(this) }
    }

    fun <T> Maybe<T>.safeSubscribe(
        onSuccess: (T) -> Unit,
        onComplete: (() -> Unit)? = null,
        onNetworkConnectionError: ((Throwable) -> Unit)? = null,
        onServerError: ((ru.shipa.app.data.exception.ServerException) -> Unit)? = null,
        onError: ((ApplicationError) -> Unit)? = null
    ): Disposable {

        return smartSubscribe(
            onSuccess,
            onComplete,
            onNetworkConnectionError,
            onServerError,
            onError
        ).apply { compositeDisposable.add(this) }
    }
}
