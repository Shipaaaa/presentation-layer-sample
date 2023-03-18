package ru.shipa.app

import android.app.Application
import ru.shipa.app.infrastructure.logging.PrettyLoggingTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initTimber()
    }

    private fun initTimber() {
//        @Suppress("ConstantConditionIf")
//        if (BuildConfig.DEBUG) {
//            Timber.plant(PrettyLoggingTree(this))
//        }
    }

}
