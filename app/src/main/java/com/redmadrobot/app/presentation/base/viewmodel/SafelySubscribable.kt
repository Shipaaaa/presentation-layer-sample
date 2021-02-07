package com.redmadrobot.app.presentation.base.viewmodel

import com.redmadrobot.app.data.exception.ServerException
import com.redmadrobot.app.extension.ApplicationError
import com.redmadrobot.app.extension.smartSubscribe
import com.redmadrobot.app.presentation.utils.StringResource
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface SafelySubscribable {

    val compositeDisposable: CompositeDisposable

    fun <T> Single<T>.safeSubscribe(
        onSuccess: (T) -> Unit,
        onNetworkConnectionError: ((Throwable) -> Unit)? = null,
        onServerError: ((ServerException) -> Unit)? = null,
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
        onServerError: ((ServerException) -> Unit)? = null,
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
        onServerError: ((ServerException) -> Unit)? = null,
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
        onServerError: ((ServerException) -> Unit)? = null,
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
        onServerError: ((ServerException) -> Unit)? = null,
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
