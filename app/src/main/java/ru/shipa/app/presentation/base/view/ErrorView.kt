package ru.shipa.app.presentation.base.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.StringRes
import ru.shipa.app.R
import ru.shipa.app.databinding.ViewErrorBinding
import ru.shipa.app.extension.makeGone
import ru.shipa.app.extension.makeVisible

class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewBinding: ViewErrorBinding = ViewErrorBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private var errorImage: Drawable? = null
        set(value) {
            field = value
            field?.let { viewBinding.viewErrorImage.setImageDrawable(it) }
        }

    private var errorTitle = ""
        set(value) {
            field = value
            viewBinding.viewErrorTextViewTitle.text = field
        }

    private var errorDescription: String? = null
        set(value) {
            field = value
            if (value != null) {
                viewBinding.viewErrorTextViewDescription.text = field
                viewBinding.viewErrorTextViewDescription.makeVisible()
            } else {
                viewBinding.viewErrorTextViewDescription.makeGone()
            }
        }

    private var retryButtonCaption = ""
        set(value) {
            field = value
            viewBinding.viewErrorButtonRetry.text = field
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
        viewBinding.viewErrorButtonRetry.setOnClickListener { listener() }
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
