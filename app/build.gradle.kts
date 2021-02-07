plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.daggerHiltAndroidPlugin)
    id(BuildPlugins.navigationSafeArgs)
}

android {

    compileSdkVersion(AndroidSdk.compile)

    defaultConfig {
        applicationId = "com.redmadrobot.app"

        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)

        versionCode = 1
        versionName = "1.0"

        resConfig("ru")
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true

            isMinifyEnabled = false
            isShrinkResources = false
        }

        getByName("release") {
            isDebuggable = false

            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    androidExtensions {
        isExperimental = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

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
    implementation(Libraries.androidxHiltLifecycle)

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
