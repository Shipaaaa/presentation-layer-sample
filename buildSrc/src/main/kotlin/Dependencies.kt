const val kotlinVersion = "1.8.10"
const val navigationVersion = "2.5.3"
const val hiltVersion = "2.45"

object BuildPlugins {

    object Versions {
        const val androidApplicationVersion = "8.0.0-beta05"
    }

    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"

    const val kotlinAndroid = "org.jetbrains.kotlin.android"
    const val kotlinKapt = "kotlin-kapt"
    const val kotlinParcelize = "kotlin-parcelize"
    const val daggerHiltAndroidPlugin = "com.google.dagger.hilt.android"
    const val navigationSafeArgs = "androidx.navigation.safeargs"

    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val navigationSafeArgsGradlePlugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:$navigationVersion"
    const val daggerHiltAndroidGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$hiltVersion"
}

object AndroidSdk {
    const val min = 24
    const val compile = 33
    const val target = compile
}

object Libraries {

    private object Versions {
        const val ktx = "1.9.0"

        const val jetpack = "1.6.1"
        const val constraintLayout = "2.1.4"
        const val swipeRefreshLayout = "1.1.0"
        const val material = "1.8.0"

        const val lifecycle = "2.2.0"

        const val daggerHiltLifecycle = "1.0.0-alpha01"

        const val rxJava2 = "2.2.19"
        const val rxAndroid = "2.1.1"
        const val rxRelay = "2.1.1"

        const val groupie = "2.8.0"

        const val glide = "4.11.0"

        const val stateDelegator = "1.7"

        const val timber = "4.7.1"
        const val logger = "2.2.0"
    }

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktx}"

    const val appCompat = "androidx.appcompat:appcompat:${Versions.jetpack}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}"
    const val material = "com.google.android.material:material:${Versions.material}"

    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"

    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:$navigationVersion"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:$navigationVersion"

    const val daggerHilt = "com.google.dagger:hilt-android:$hiltVersion"
    const val daggerHiltCompiler = "com.google.dagger:hilt-android-compiler:$hiltVersion"

    const val androidxHiltCompiler = "androidx.hilt:hilt-compiler:${Versions.daggerHiltLifecycle}"

    const val rxjava2 = "io.reactivex.rxjava2:rxjava:${Versions.rxJava2}"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    const val rxRelay = "com.jakewharton.rxrelay2:rxrelay:${Versions.rxRelay}"

    const val groupie = "com.github.lisawray.groupie:groupie:${Versions.groupie}"
    const val groupieViewBinding = "com.github.lisawray.groupie:groupie-viewbinding:${Versions.groupie}"

    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    const val stateDelegator = "com.redmadrobot:state-delegator:${Versions.stateDelegator}"

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val logger = "com.orhanobut:logger:${Versions.logger}"
}
