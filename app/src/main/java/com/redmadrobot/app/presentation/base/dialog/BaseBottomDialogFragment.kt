package com.redmadrobot.app.presentation.base.dialog

import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomDialogFragment : BottomSheetDialogFragment() {

    abstract fun getDialogTag(): String

    fun show(fragmentManager: FragmentManager) {
        val ft = fragmentManager.beginTransaction()
        val prev = fragmentManager.findFragmentByTag(getDialogTag())
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        this.show(ft, getDialogTag())
    }

}
