package ru.shipa.app.presentation.base.dialog

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
import ru.shipa.app.R
import ru.shipa.app.databinding.FragmentDialogAlertBinding

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
        val viewBinding: FragmentDialogAlertBinding = FragmentDialogAlertBinding.inflate(
            layoutInflater, null, true
        )


        val titleView = viewBinding.fragmentAlertDialogTitle
        val messageView = viewBinding.fragmentAlertDialogMessage
        val buttonsContainer = viewBinding.fragmentDialogAlertContainerButtons
        val dialogBackground = viewBinding.fragmentAlertDialogBackground
        val buttons = getButtonConfig()

        titleView.text = getTitle()

        messageView.text = getMessage()

        buttonsContainer.orientation = if (isHorizontalButtons()) HORIZONTAL else VERTICAL

        buttons.forEachIndexed { _, config ->
            val button = layoutInflater
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

        return viewBinding.root
    }

    data class AlertDialogButtonConfig(
        val label: String,
        @ColorRes val colorId: Int = R.color.colorPrimary,
        val action: (() -> Unit)?
    )

}
