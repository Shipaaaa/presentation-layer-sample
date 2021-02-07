package com.redmadrobot.app.extension.viewbinding

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Method
import kotlin.reflect.KClass


/**
 * Инфлейт ViewBinding заданного типа [T].
 *
 * В качестве родителя используется [ViewGroup], по умолчанию view прикрепляется к корню родителя.
 * **ВАЖНО!** Для инфлейта вьюх с `merge` в корне нужно использовать только этот метод.
 */
inline fun <reified T : ViewBinding> ViewGroup.inflateViewBinding(
    context: Context = this.context,
    attachToRoot: Boolean = true
): T {
    return T::class.inflate(LayoutInflater.from(context), this, attachToRoot)
}

/**
 * Инфлейт ViewBinding заданного типа [T].
 *
 * Метод для случая если нет [ViewGroup] или готового [LayoutInflater], в этом случае можно передать
 * контекст из которого будет получен [LayoutInflater].
 */
inline fun <reified T : ViewBinding> Context.inflateViewBinding(
    parent: ViewGroup? = null,
    attachToRoot: Boolean = parent != null
): T {
    return T::class.inflate(LayoutInflater.from(this), parent, attachToRoot)
}

/**
 * Инфлейт ViewBinding заданного типа [T], с использованием заданного [LayoutInflater].
 * @sample com.openbank.library.dialog.CommonDialog.onCreateView
 */
inline fun <reified T : ViewBinding> LayoutInflater.inflateViewBinding(
    parent: ViewGroup? = null,
    attachToRoot: Boolean = parent != null
): T {
    return T::class.inflate(this, parent, attachToRoot)
}

/**
 * Динамический вызов метода inflate у ViewBinding.
 *
 * При помощи этого метода можно вызвать inflate у любого ViewBinding, что делает возможным
 * упрощение этого вызова.
 * @see inflateViewBinding
 */
fun <T : ViewBinding> KClass<T>.inflate(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    attachToRoot: Boolean
): T {
    val inflateMethod = java.getInflateMethod()
    @Suppress("UNCHECKED_CAST")
    return if (inflateMethod.parameterTypes.size > 2) {
        inflateMethod.invoke(null, inflater, parent, attachToRoot)
    } else {
        if (!attachToRoot) Log.d("ViewBinding", "attachToRoot is always true for ${java.simpleName}.inflate")
        inflateMethod.invoke(null, inflater, parent)
    } as T
}

private val inflateMethodsCache = mutableMapOf<Class<out ViewBinding>, Method>()

private fun Class<out ViewBinding>.getInflateMethod(): Method {
    return inflateMethodsCache.getOrPut(this) {
        declaredMethods.find { method ->
            val parameterTypes = method.parameterTypes
            method.name == "inflate" &&
                    parameterTypes[0] == LayoutInflater::class.java &&
                    parameterTypes.getOrNull(1) == ViewGroup::class.java &&
                    (parameterTypes.size == 2 || parameterTypes[2] == Boolean::class.javaPrimitiveType)
        } ?: error("Method ${this.simpleName}.inflate(LayoutInflater, ViewGroup[, boolean]) not found.")
    }
}

/**
 * Получение биндинга заданного типа [T] из [View].
 */
inline fun <reified T : ViewBinding> View.getBinding(): T = T::class.bind(this)
