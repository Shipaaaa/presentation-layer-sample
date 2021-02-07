package com.redmadrobot.app.presentation.base.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import android.widget.LinearLayout.VERTICAL
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.redmadrobot.app.R
import kotlinx.android.synthetic.main.fragment_dialog_alert.view.*

abstract class BaseAlertDialogFragment : DialogFragment() {

    abstract fun getTitle(): String

    abstract fun getMessage(): String

    abstract fun getButtonConfig(): List<AlertDialogButtonConfig>

    open fun isHorizontalButtons(): Boolean = true

    open fun onTouchOutsideClick() {
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        with(requireDialog()) {
            // Для корректной отрисовки диалога
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val alertDialog = LayoutInflater.from(context).inflate(R.layout.fragment_dialog_alert, container)

        val titleView = alertDialog.fragment_alert_dialog_title
        val messageView = alertDialog.fragment_alert_dialog_message
        val buttonsContainer = alertDialog.fragment_dialog_alert_container_buttons
        val dialogBackground = alertDialog.fragment_alert_dialog_background
        val buttons = getButtonConfig()

        titleView.text = getTitle()

        messageView.text = getMessage()

        buttonsContainer.orientation = if (isHorizontalButtons()) HORIZONTAL else VERTICAL

        buttons.forEachIndexed { _, config ->
            val button = LayoutInflater.from(context)
                .inflate(R.layout.layout_alert_dialog_fragment_button, container) as Button

            with(button) {
                text = config.label
                setTextColor(ContextCompat.getColor(requireContext(), config.colorId))
                setOnClickListener { config.action?.invoke() }

                layoutParams = LinearLayout
                    .LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.END
                    }

                buttonsContainer.addView(this)
            }
        }

        // По клику на тень закрываем диалог
        dialogBackground.setOnClickListener { onTouchOutsideClick() }

        return alertDialog
    }

    data class AlertDialogButtonConfig(
        val label: String,
        @ColorRes val colorId: Int = R.color.colorPrimary,
        val action: (() -> Unit)?
    )

}
