@file:Suppress("UnstableApiUsage")

plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.kotlinParcelize)
    id(BuildPlugins.daggerHiltAndroidPlugin)
    id(BuildPlugins.navigationSafeArgs)
}

android {
    namespace = "ru.shipa.app"
    compileSdk = AndroidSdk.compile

    defaultConfig {
        applicationId = "ru.shipa.app"

        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target

        versionCode = 1
        versionName = "1.0"

        resConfig("ru")
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isDebuggable = true

            isMinifyEnabled = false
            isShrinkResources = false
        }

        release {
            isDebuggable = false

            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        viewBinding = true
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.ktxCore)

    implementation(Libraries.appCompat)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.swipeRefreshLayout)
    implementation(Libraries.material)

    implementation(Libraries.lifecycleExtensions)
    implementation(Libraries.liveDataKtx)

    implementation(Libraries.navigationFragment)
    implementation(Libraries.navigationUi)

    implementation(Libraries.daggerHilt)

    implementation(Libraries.rxjava2)
    implementation(Libraries.rxAndroid)
    implementation(Libraries.rxRelay)

    implementation(Libraries.groupie)
    implementation(Libraries.groupieViewBinding)

    implementation(Libraries.glide)

    implementation(Libraries.stateDelegator)

    implementation(Libraries.timber)
    implementation(Libraries.logger)

    kapt(Libraries.daggerHiltCompiler)
    kapt(Libraries.androidxHiltCompiler)
    kapt(Libraries.glideCompiler)
}
