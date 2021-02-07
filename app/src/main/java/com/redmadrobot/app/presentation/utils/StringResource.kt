package com.redmadrobot.app.presentation.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 * Приводит к одному типу строку или id строки в ресурсах, позволяя работать с ними одинаково.
 *
 * ВНИМАНИЕ! Класс намеренно не сделан Parcelable/Serializable.
 */
data class StringResource(
    private val string: String,
    @StringRes
    private val stringRes: Int? = null
) {

    fun get(): String = string

    fun get(context: Context): String = stringRes?.let { context.getString(it) } ?: string

}

fun Fragment.getString(stringResource: StringResource): String = stringResource.get(requireContext())

fun Fragment.getStringOrNull(stringResource: StringResource?): String? = context?.let { stringResource?.get(it) }
