package com.redmadrobot.app.presentation.base.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.StringRes
import com.redmadrobot.app.R
import com.redmadrobot.app.extension.makeGone
import com.redmadrobot.app.extension.makeVisible
import kotlinx.android.synthetic.main.view_error.view.*

class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var errorImage: Drawable? = null
        set(value) {
            field = value
            field?.let { view_error_image.setImageDrawable(it) }
        }

    private var errorTitle = ""
        set(value) {
            field = value
            view_error_text_view_title.text = field
        }

    private var errorDescription: String? = null
        set(value) {
            field = value
            if (value != null) {
                view_error_text_view_description.text = field
                view_error_text_view_description.makeVisible()
            } else {
                view_error_text_view_description.makeGone()
            }
        }

    private var retryButtonCaption = ""
        set(value) {
            field = value
            view_error_button_retry.text = field
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_error, this, true)

        lateinit var attrsArray: TypedArray

        try {
            attrsArray = context.obtainStyledAttributes(attrs, R.styleable.ErrorView)
            with(attrsArray) {
                errorImage = getDrawable(R.styleable.ErrorView_image)
                errorTitle = getString(R.styleable.ErrorView_errorTitle) ?: ""
                errorDescription = getString(R.styleable.ErrorView_errorDescription)
                retryButtonCaption = getString(R.styleable.ErrorView_retryButtonCaption) ?: ""
            }

        } finally {
            attrsArray.recycle()
        }
    }

    fun setOnRetryClickListener(listener: () -> Unit) {
        view_error_button_retry.setOnClickListener { listener() }
    }

    fun showError(
        isNetworkException: Boolean,
        @StringRes
        internetErrorTitle: Int = R.string.error_no_internet,
        @StringRes
        dataErrorTitle: Int = R.string.error_data_unavailable
    ) {

        val image = if (isNetworkException) R.mipmap.ic_launcher else R.mipmap.ic_launcher
        errorImage = context.getDrawable(image)

        val title = if (isNetworkException) internetErrorTitle else dataErrorTitle
        errorTitle = context.getString(title)

        errorDescription = if (!isNetworkException) {
            context.getString(R.string.error_data_unavailable_description)
        } else {
            null
        }

        val retryButtonText = R.string.try_again
        retryButtonCaption = context.getString(retryButtonText)
    }
}
