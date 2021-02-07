package com.redmadrobot.app.extension

import com.redmadrobot.app.R
import com.redmadrobot.app.data.exception.ServerException
import com.redmadrobot.app.presentation.utils.StringResource
import io.reactivex.*
import io.reactivex.disposables.Disposable
import java.net.*

fun <T> Single<T>.smartSubscribe(
    onSuccess: (T) -> Unit,
    onNetworkConnectionError: ((Throwable) -> Unit)? = null,
    onServerError: ((ServerException) -> Unit)? = null,
    onError: ((ApplicationError) -> Unit)? = null
): Disposable {

    return subscribe(
        { onSuccess(it) },
        { throwable ->
            throwable.processError(
                onNetworkConnectionError,
                onServerError,
                onError
            )
        }
    )
}

@Suppress("LongParameterList")
fun <T> Observable<T>.smartSubscribe(
    onSuccess: (T) -> Unit,
    onComplete: (() -> Unit)? = null,
    onNetworkConnectionError: ((Throwable) -> Unit)? = null,
    onServerError: ((ServerException) -> Unit)? = null,
    onError: ((ApplicationError) -> Unit)? = null
): Disposable {

    return subscribe(
        { onSuccess(it) },
        { throwable ->
            throwable.processError(
                onNetworkConnectionError,
                onServerError,
                onError
            )
        },
        { onComplete?.invoke() }
    )
}

fun <T> Flowable<T>.smartSubscribe(
    onSuccess: (T) -> Unit,
    onNetworkConnectionError: ((Throwable) -> Unit)? = null,
    onServerError: ((ServerException) -> Unit)? = null,
    onError: ((ApplicationError) -> Unit)? = null
): Disposable {

    return subscribe(
        { onSuccess(it) },
        { throwable ->
            throwable.processError(
                onNetworkConnectionError,
                onServerError,
                onError
            )
        }
    )
}

fun Completable.smartSubscribe(
    onComplete: (() -> Unit)? = null,
    onNetworkConnectionError: ((Throwable) -> Unit)? = null,
    onServerError: ((ServerException) -> Unit)? = null,
    onError: ((ApplicationError) -> Unit)? = null
): Disposable {

    return subscribe({ onComplete?.invoke() }, { throwable ->
        throwable.processError(
            onNetworkConnectionError,
            onServerError,
            onError
        )
    })
}

@Suppress("LongParameterList")
fun <T> Maybe<T>.smartSubscribe(
    onSuccess: (T) -> Unit,
    onComplete: (() -> Unit)? = null,
    onNetworkConnectionError: ((Throwable) -> Unit)? = null,
    onServerError: ((ServerException) -> Unit)? = null,
    onError: ((ApplicationError) -> Unit)? = null
): Disposable {

    return subscribe(
        { onSuccess(it) },
        { throwable ->
            throwable.processError(
                onNetworkConnectionError,
                onServerError,
                onError
            )
        },
        { onComplete?.invoke() }
    )
}

fun Throwable.processError(
    onNetworkConnectionError: ((Throwable) -> Unit)? = null,
    onServerError: ((ServerException) -> Unit)? = null,
    onError: ((ApplicationError) -> Unit)? = null
) {
    var message = StringResource("")
    var title = StringResource("")

    when {
        this.isNetworkException() -> {
            if (onNetworkConnectionError == null) {

                message = StringResource("R.string.error_no_internet", R.string.error_no_internet)
            } else {
                onNetworkConnectionError.invoke(this)
            }
        }
        this is ServerException -> {
            if (onServerError == null) {
                message = StringResource(this.description)
                title = StringResource(this.title)
            } else {
                onServerError.invoke(this)
            }
        }
        else -> {
            message = StringResource("R.string.error_default", R.string.error_default)
        }
    }

    onError?.invoke(ApplicationError(this, message, title))
}

data class ApplicationError(
    val throwable: Throwable,
    val message: StringResource,
    val title: StringResource
)

fun Throwable?.isNetworkException(): Boolean {
    return when (this) {
        is ConnectException,
        is SocketException,
        is SocketTimeoutException,
        is UnknownHostException,
        is ProtocolException -> true
        else -> false
    }
}
