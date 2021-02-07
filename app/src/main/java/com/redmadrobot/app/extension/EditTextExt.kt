package com.redmadrobot.app.extension

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

fun EditText.setOnTextChangeListener(listener: (String) -> Unit): TextWatcher {
    return object : TextWatcher {

        override fun afterTextChanged(s: Editable) {
            /* ignored */
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            /* ignored */
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            listener.invoke(s.toString())
        }

    }.apply { addTextChangedListener(this) }
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.setOnRightDrawableClickListener(listener: (view: EditText) -> Unit) {
    this.setOnTouchListener { v, event ->
        var hasConsumed = false
        if (v is EditText && event.x >= v.width - v.totalPaddingRight) {
            if (event.action == MotionEvent.ACTION_UP) {
                listener(this)
            }
            hasConsumed = true
        }
        hasConsumed
    }
}

fun TextInputLayout.showError(errorMessage: String = " ") {
    error = errorMessage
}

fun TextInputLayout.resetError() {
    error = null
}

fun EditText.text() = this.text.toString()

fun EditText.clear() = this.setText("")

fun EditText.setOnDoneActionListener(listener: (view: EditText) -> Boolean) {
    this.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            listener.invoke(this)
        } else {
            false
        }
    }
}
