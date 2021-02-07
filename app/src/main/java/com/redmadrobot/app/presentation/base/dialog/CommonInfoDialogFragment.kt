package com.redmadrobot.app.presentation.base.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redmadrobot.app.R

class CommonInfoDialogFragment : BaseAlertDialogFragment() {

    companion object {
        const val TAG = "CommonInfoDialogFragment"

        private const val TITLE = "title"
        private const val MESSAGE = "message"

        fun newInstance(title: String, message: String): CommonInfoDialogFragment {
            return CommonInfoDialogFragment().apply {
                isCancelable = false

                arguments = Bundle().apply {
                    putString(TITLE, title)
                    putString(MESSAGE, message)
                }
            }
        }
    }

    private lateinit var title: String
    private lateinit var message: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        arguments?.apply {
            title = getString(TITLE, "")
            message = getString(MESSAGE, "")
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun isCancelable() = false

    override fun getTitle() = title

    override fun getMessage() = message

    override fun getButtonConfig(): List<AlertDialogButtonConfig> {
        return listOf(
            AlertDialogButtonConfig(
                label = getString(R.string.close),
                action = { dismiss() }
            )
        )
    }
}
