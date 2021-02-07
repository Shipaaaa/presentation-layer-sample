package com.redmadrobot.app.presentation.base.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import com.redmadrobot.app.R
import com.redmadrobot.app.extension.makeGone
import com.redmadrobot.app.extension.makeVisible
import kotlinx.android.synthetic.main.view_stub.view.*

class StubView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    @DrawableRes
    var stubImage: Int = R.mipmap.ic_launcher
        set(value) {
            field = value
            view_stub_image.setImageResource(field)
        }

    var stubTitle: String = ""
        set(value) {
            field = value
            view_stub_text_view_title.text = field
        }

    var stubDescription: String = ""
        set(value) {
            field = value
            view_stub_text_view_description.text = field
        }

    private var addButtonCaption: String? = null
        set(value) {
            field = value
            if (value != null) {
                view_stub_button_add.text = field
                view_stub_button_add.makeVisible()
            } else {
                view_stub_button_add.makeGone()
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
        view_stub_button_add.setOnClickListener { listener() }
    }

}
