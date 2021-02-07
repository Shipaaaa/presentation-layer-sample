package com.redmadrobot.app.presentation.base.viewmodel.state

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.redmadrobot.app.extension.ApplicationError
import com.redmadrobot.app.presentation.utils.StringResource

sealed class LceState<T> : BaseState {

    fun asContent(): T {
        return (this as Content<T>).content
    }

    fun asContentOrNull(): T? {
        return (this as? Content<T>)?.content
    }

}

class Loading<T> : LceState<T>()

/*Content*/
data class Content<T>(val content: T) : LceState<T>()
/**/

/*Stub*/
data class Stub<T>(
    val message: String,
    @DrawableRes val image: Int? = null,
    @StringRes val buttonText: Int? = null,
    val onClicked: (() -> Unit)? = null
) : LceState<T>()
/**/

/*Error*/
data class Error<T>(
    val exception: Throwable,
    val message: StringResource,
    val title: StringResource
) : LceState<T>() {

    companion object {
        // TODO Избавиться от контекста.
        fun <T> fromAppError(error: ApplicationError): Error<T> {
            return Error(
                error.throwable,
                error.message,
                error.title
            )
        }
    }

}
/**/
