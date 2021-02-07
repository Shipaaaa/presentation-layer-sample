@file:Suppress("unused", "TooManyFunctions")

package com.redmadrobot.app.extension

import android.app.Activity
import android.content.Context
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.Group
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.google.android.material.snackbar.Snackbar
import com.redmadrobot.app.R
import com.redmadrobot.app.presentation.utils.GlideApp
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun View.setKeyboardFocusViewImmediate() {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.hideKeyboard() {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    val view = this.currentFocus?.let { it } ?: View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun View.makeVisibleOrGone(visible: Boolean) {
    if (visible) makeVisible() else makeGone()
}

fun View.makeVisibleOrInvisible(visible: Boolean) {
    if (visible) makeVisible() else makeInvisible()
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

@Suppress("LongParameterList")
fun ImageView.setImageFromUrl(
    url: String?,
    @DrawableRes placeholderId: Int? = null,
    placeholderDrawable: Drawable? = null,
    placeholderWithAnimations: Boolean = true,
    errorPlaceholderDrawable: Drawable? = null,
    customAction: ((RequestBuilder<Drawable>) -> RequestBuilder<Drawable>)? = null
) {

    GlideApp
        .with(this)
        .load(url)
        .apply { customAction?.invoke(this) }
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .apply {
            placeholderId?.let { placeholder(ContextCompat.getDrawable(context, it)) }
            placeholderDrawable?.let { placeholder(it) }
        }
        .apply { errorPlaceholderDrawable?.let { error(it) } }
        .apply {
            if (placeholderWithAnimations) {
                transition(
                    DrawableTransitionOptions
                        .withCrossFade()
                        .crossFade(
                            // Для фикса бага с наложением плейсхолдера и изображения
                            // Подробности: http://bumptech.github.io/glide/doc/transitions.html#cross-fading-with-placeholders-and-transparent-images
                            DrawableCrossFadeFactory
                                .Builder()
                                .setCrossFadeEnabled(true)
                                .build()
                        )
                )
            }
        }
        .into(this)
}

fun Int.dpToPx() = (this * Resources.getSystem().displayMetrics.density).toInt()
fun Float.dpToPx() = this * Resources.getSystem().displayMetrics.density
fun Int.pxToDp() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Group.setAllOnClickListener(listener: View.OnClickListener?) {
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).setOnClickListener(listener)
    }
}

fun Group.setAllOnClickListener(listener: (view: View) -> Unit) {
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).setOnClickListener(listener)
    }
}

/**
 * Исправление проблемы с fitsSystemWindow.
 * По умолчанию информация об insets не передаётся дочерним view,
 * поэтому происходят ошибки при использование fitsSystemWindow с fragments.
 * Этот метод передаёт информацию об insets в дочернии view.
 *
 * Подробности тут: https://medium.com/androiddevelopers/windows-insets-fragment-transitions-9024b239a436
 */
fun View.dispatchApplyWindowInsetsToChild() {
    setOnApplyWindowInsetsListener { view, insets ->
        var consumed = false

        (view as? ViewGroup)?.forEach { child ->
            val childResult = child.dispatchApplyWindowInsets(insets)
            if (childResult.isConsumed) consumed = true
        }

        if (consumed) insets.consumeSystemWindowInsets() else insets
    }
}

inline fun View.onGlobalLayoutHappen(crossinline action: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            action.invoke()
        }
    })
}

private const val SNACKBAR_WIDTH_DP_IN_LANDSCAPE_ORIENTATION = 344

/**
 * Метод отвечает за изменение внешнего вида [Snackbar].
 *
 * Метод изменяет цвет [Snackbar] через метод [Snackbar.SnackbarLayout.setBackgroundTintList] и
 * текстовые стили у [R.id.snackbar_text] и [R.id.snackbar_action]
 *
 * Также в методе содержится изменение ширины [Snackbar] для горизонтального режима.
 *
 * Для изменения ширины используется [CoordinatorLayout.LayoutParams].
 * [CoordinatorLayout.LayoutParams] имеет атрибут [CoordinatorLayout.LayoutParams.gravity],
 * которого нет в [ViewGroup.LayoutParams],
 * поэтому изменение будет выполнятся, только если корневым ViewGroup для [Snackbar] будет [CoordinatorLayout].
 *
 * @param backgroundId id цвета фона из ресуров, например [android.R.color.white].
 * @param textColorId id цвета текста из ресуров, например [android.R.color.black].
 */
fun Snackbar.decorate(@ColorRes backgroundId: Int, @ColorRes textColorId: Int): Snackbar {
    // Изменение ширины Snackbar для горизонтального режима.
    val orientation = context.resources.configuration.orientation
    val snackbarLayoutParams = view.layoutParams

    if (orientation == ORIENTATION_LANDSCAPE && snackbarLayoutParams is CoordinatorLayout.LayoutParams) {
        val customParams = snackbarLayoutParams.apply {
            width = SNACKBAR_WIDTH_DP_IN_LANDSCAPE_ORIENTATION.dpToPx()
            gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        }
        view.layoutParams = customParams
    }

    // Изменение фона
    view.backgroundTintList = ContextCompat.getColorStateList(view.context, backgroundId)

    // Изменение текстового стиля
    with(view.findViewById<TextView>(R.id.snackbar_text)) {
        setTextColor(ContextCompat.getColor(view.context, textColorId))
        maxLines = Int.MAX_VALUE
        ellipsize = null
    }

    // Изменение текстового стиля
    with(view.findViewById<TextView>(R.id.snackbar_action)) {
        setTextColor(ContextCompat.getColor(view.context, textColorId))
    }

    return this
}

/**
 * Метод удаляет adapter из [RecyclerView], чтобы избежать утечки памяти.
 *
 * Подробнее:
 * https://github.com/airbnb/epoxy/wiki/Avoiding-Memory-Leaks
 * https://stackoverflow.com/a/46957469/5484505
 */
fun RecyclerView.removeAdapterOnViewDetachedFromWindow() {
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {
            // Do nothing
        }

        override fun onViewDetachedFromWindow(v: View) {
            adapter = null
        }
    })
}

fun View.isAnimationFinished() = animation == null || animation.hasEnded()

fun (() -> Unit).toDelayObserver(delaySeconds: Long): Completable {
    return toMillisDelayObserver(delayMillis = TimeUnit.SECONDS.toMillis(delaySeconds))
}

fun (() -> Unit).toMillisDelayObserver(delayMillis: Long): Completable {
    return Completable.timer(delayMillis, TimeUnit.MILLISECONDS, Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnComplete { this() }
}

fun View.setBackgroundTint(@ColorRes color: Int?) {
    if (color == null) {
        backgroundTintList = null
    } else {
        background.tint(context, color)
    }
}

fun View.getColor(@ColorRes color: Int): Int {
    return ContextCompat.getColor(context, color)
}

fun View.getDimensionPixelSize(@DimenRes dimenRes: Int): Int {
    return context.resources.getDimensionPixelSize(dimenRes)
}

fun View.getDrawable(@DrawableRes drawableRes: Int): Drawable? {
    return AppCompatResources.getDrawable(context, drawableRes)
}
