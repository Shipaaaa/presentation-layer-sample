package ru.shipa.app.presentation.utils

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

const val DEFAULT_KEY = "androidx.lifecycle.ViewModelProvider.DefaultKey"

inline fun <reified T : ViewModel> ViewModelStoreOwner.getViewModel(factory: ViewModelProvider.Factory): T {
    val provider = ViewModelProvider(this, factory)
    return provider[T::class.java]
}

inline fun <reified T : ViewModel> ViewModelStore.getViewModel(factory: ViewModelProvider.Factory): T {
    val provider = ViewModelProvider(this, factory)
    return provider[T::class.java]
}

/** Возвращает [ViewModel] из [factory] по [key]  */
inline fun <reified T : ViewModel> ViewModelStore.getViewModel(
    key: String,
    factory: ViewModelProvider.Factory
): T {
    val canonicalName: String = T::class.java.canonicalName ?: DEFAULT_KEY
    val provider = ViewModelProvider(this, factory)
    return provider.get("$key $canonicalName", T::class.java)
}

/**
 * Делегат для удобного Lazy создания ViewModel во фрагменте
 * @param viewModelProducer - функция, возвращающая ViewModel
 *
 * Пример использования:
 *  class QuikFragment {
 *      @Inject
 *      lateinit var viewModelProvider: Provider<TransferByPhoneViewModel>
 *
 *      private val viewModel by viewModels { viewModelProvider.get() }
 *  }
 */
@MainThread
inline fun <reified VM : ViewModel> Fragment.viewModels(
    crossinline viewModelProducer: () -> VM
): Lazy<VM> {
    return lazy(LazyThreadSafetyMode.NONE) {
        createViewModel({ viewModelProducer() }, { this.viewModelStore })
    }
}

/**
 * Делегат для удобного создания Shared-ViewModel во фрагменте
 * ViewModel привязана к Activity и может переиспользоваться на нескольких экранах в рамках
 * этой Activity
 */
@MainThread
inline fun <reified VM : ViewModel> Fragment.sharedViewModels(
    crossinline viewModelProducer: () -> VM
): Lazy<VM> {
    return lazy(LazyThreadSafetyMode.NONE) {
        createViewModel({ viewModelProducer() }, { requireActivity().viewModelStore })
    }
}

@MainThread
inline fun <reified VM : ViewModel> Fragment.createViewModel(
    crossinline viewModelProducer: () -> VM,
    crossinline storeProducer: () -> ViewModelStore
): VM {
    return initViewModel(viewModelProducer, storeProducer)
}

/**
 * Делегат для удобного Lazy создания ViewModel во фрагменте
 * @param viewModelProducer - функция, возвращающая ViewModel
 *
 * Пример использования:
 *  class TransferByPhoneActivity {
 *
 *      @Inject
 *      lateinit var viewModelProvider: Provider<TransferByPhoneViewModel>
 *
 *      private val viewModel by viewModels { viewModelProvider.get() }
 *
 *  }
 */
@MainThread
inline fun <reified VM : ViewModel> FragmentActivity.viewModels(
    crossinline viewModelProducer: () -> VM
): Lazy<VM> {
    return lazy(LazyThreadSafetyMode.NONE) {
        createViewModel({ viewModelProducer() }, { this.viewModelStore })
    }
}

@MainThread
inline fun <reified VM : ViewModel> FragmentActivity.createViewModel(
    crossinline viewModelProducer: () -> VM,
    crossinline storeProducer: () -> ViewModelStore
): VM {
    return initViewModel(viewModelProducer, storeProducer)
}

inline fun <reified VM : ViewModel> initViewModel(
    crossinline viewModelProducer: () -> VM,
    crossinline storeProducer: () -> ViewModelStore
): VM {
    val factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <VM : ViewModel> create(modelClass: Class<VM>) = viewModelProducer() as VM
    }
    val viewModelProvider = ViewModelProvider(storeProducer(), factory)
    return viewModelProvider[VM::class.java]
}