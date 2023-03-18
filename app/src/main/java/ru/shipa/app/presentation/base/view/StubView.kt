package ru.shipa.app.presentation.base.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import ru.shipa.app.R
import ru.shipa.app.databinding.ViewStubBinding
import ru.shipa.app.extension.makeGone
import ru.shipa.app.extension.makeVisible

class StubView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewBinding: ViewStubBinding = ViewStubBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    @DrawableRes
    var stubImage: Int = R.mipmap.ic_launcher
        set(value) {
            field = value
            viewBinding.viewStubImage.setImageResource(field)
        }

    var stubTitle: String = ""
        set(value) {
            field = value
            viewBinding.viewStubTextViewTitle.text = field
        }

    var stubDescription: String = ""
        set(value) {
            field = value
            viewBinding.viewStubTextViewDescription.text = field
        }

    private var addButtonCaption: String? = null
        set(value) {
            field = value
            if (value != null) {
                viewBinding.viewStubButtonAdd.text = field
                viewBinding.viewStubButtonAdd.makeVisible()
            } else {
                viewBinding.viewStubButtonAdd.makeGone()
            }
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_stub, this, true)

        lateinit var attrsArray: TypedArray

        try {
            attrsArray = context.obtainStyledAttributes(attrs, R.styleable.StubView)
            with(attrsArray) {
                stubTitle = getString(R.styleable.StubView_stubTitle) ?: ""
                stubDescription = getString(R.styleable.StubView_stubDescription) ?: ""
                stubImage = getResourceId(R.styleable.StubView_stubIcon, R.mipmap.ic_launcher)
                addButtonCaption = getString(R.styleable.StubView_addButtonCaption)
            }

        } finally {
            attrsArray.recycle()
        }
    }

    fun setOnAddClickListener(listener: () -> Unit) {
        viewBinding.viewStubButtonAdd.setOnClickListener { listener() }
    }

}
