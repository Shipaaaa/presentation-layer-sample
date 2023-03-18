@file:Suppress("unused")

package ru.shipa.app.presentation.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import ru.shipa.app.extension.makeInvisible
import ru.shipa.app.extension.makeVisible
import kotlin.math.max

// TODO Возможно здесь нужно оставить только методы create, а все start перенести в новый файл AnimationsExt.
object Animations {

    const val DEFAULT_TRANSITION_DURATION_MILLIS = 300

    private const val PROPERTY_TRANSLATION_X = "translationX"

    @Suppress("MagicNumber")
    fun createShakeAnimation(view: View, animationDuration: Long = 50L): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, PROPERTY_TRANSLATION_X, 0f, -50f).apply {
            repeatCount = 3
            repeatMode = ObjectAnimator.REVERSE
            duration = animationDuration
        }
    }

    @Suppress("MagicNumber")
    fun createShiftAnimation(activity: Activity, view: View): AnimatorSet {

        val displayMetrics = DisplayMetrics()

        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)

        val screenWidth = displayMetrics.widthPixels.toFloat()

        val toLeft = ObjectAnimator.ofFloat(
            view,
            PROPERTY_TRANSLATION_X,
            0f,
            -screenWidth
        )

        val fromRight = ObjectAnimator.ofFloat(
            view,
            PROPERTY_TRANSLATION_X,
            screenWidth,
            0f
        )

        return AnimatorSet().apply {
            playSequentially(toLeft, fromRight)
            duration = 300L
            interpolator = AccelerateDecelerateInterpolator()
        }
    }

    @Suppress("MagicNumber")
    fun createBlinkAnimation(): Animation {
        return AlphaAnimation(1f, 0.5f).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            repeatCount = Animation.INFINITE
            repeatMode = Animation.REVERSE
        }
    }

    fun startSlideUpAnimation(view: View, duration: Int = DEFAULT_TRANSITION_DURATION_MILLIS) {
        view.translationY = view.height.toFloat()
        view.alpha = 0f

        view.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(duration.toLong())
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    view.makeVisible()
                }
            })
            .start()
    }

    fun startSlideDownAnimation(view: View, duration: Int = DEFAULT_TRANSITION_DURATION_MILLIS) {
        view.translationY = 0f
        view.alpha = 0f

        view.animate()
            .translationY(view.height.toFloat())
            .alpha(1f)
            .setDuration(duration.toLong())
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    view.makeVisible()
                }
            })
            .start()
    }

    fun startFadeInAnimation(
        view: View,
        duration: Int = DEFAULT_TRANSITION_DURATION_MILLIS,
        onStart: (() -> Unit)? = null,
        onEnd: (() -> Unit)? = null,
        onRepeat: (() -> Unit)? = null
    ) {
        view.startAnimation(createFadeInAnimation(duration, onStart, onEnd, onRepeat))
    }

    fun createFadeInAnimation(
        duration: Int = DEFAULT_TRANSITION_DURATION_MILLIS,
        onStart: (() -> Unit)? = null,
        onEnd: (() -> Unit)? = null,
        onRepeat: (() -> Unit)? = null
    ): Animation {
        return AlphaAnimation(0f, 1f).apply {
            this.duration = duration.toLong()
            interpolator = AccelerateDecelerateInterpolator()

            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    onStart?.invoke()
                }

                override fun onAnimationEnd(animation: Animation) {
                    onEnd?.invoke()
                }

                override fun onAnimationRepeat(animation: Animation) {
                    onRepeat?.invoke()
                }
            })
        }
    }

    fun startFadeOutAnimation(
        view: View,
        duration: Int = DEFAULT_TRANSITION_DURATION_MILLIS,
        onStart: (() -> Unit)? = null,
        onEnd: (() -> Unit)? = null,
        onRepeat: (() -> Unit)? = null
    ) {
        view.startAnimation(createFadeOutAnimation(duration, onStart, onEnd, onRepeat))
    }

    // TODO Заменить на ObjectAnimator.ofFloat(view, View.ALPHA,  1f, 0f)
    fun createFadeOutAnimation(
        duration: Int = DEFAULT_TRANSITION_DURATION_MILLIS,
        onStart: (() -> Unit)? = null,
        onEnd: (() -> Unit)? = null,
        onRepeat: (() -> Unit)? = null
    ): Animation {
        return AlphaAnimation(1f, 0f).apply {
            this.duration = duration.toLong()
            interpolator = AccelerateDecelerateInterpolator()

            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    onStart?.invoke()
                }

                override fun onAnimationEnd(animation: Animation) {
                    onEnd?.invoke()
                }

                override fun onAnimationRepeat(animation: Animation) {
                    onRepeat?.invoke()
                }
            })
        }
    }

    fun startEnterRevealAnimation(view: View) {
        val cx = view.measuredWidth / 2
        val cy = view.measuredHeight / 2

        val finalRadius = max(view.width, view.height) / 2

        view.makeVisible()
        ViewAnimationUtils
            .createCircularReveal(view, cx, cy, 0f, finalRadius.toFloat())
            .start()
    }

    fun startExitRevealAnimation(view: View) {
        val cx = view.measuredWidth / 2
        val cy = view.measuredHeight / 2

        val initialRadius = view.width / 2

        ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius.toFloat(), 0f).apply {
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    view.makeInvisible()
                }
            })
            start()
        }
    }

    fun startRotationAnimation(
        view: View,
        duration: Long = 900L,
        fromDegrees: Float = 0f,
        toDegrees: Float = 360f,
        animationListener: Animation.AnimationListener? = null
    ) {
        val rotate = RotateAnimation(
            fromDegrees,
            toDegrees,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            this.duration = duration
            repeatCount = Animation.INFINITE
        }

        animationListener?.let {
            rotate.setAnimationListener(it)
        }

        view.startAnimation(rotate)
    }
}
