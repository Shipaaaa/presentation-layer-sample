plugins {
    id(BuildPlugins.androidApplication) version BuildPlugins.Versions.androidApplicationVersion apply false
    id(BuildPlugins.androidLibrary) version BuildPlugins.Versions.androidApplicationVersion apply false
    id(BuildPlugins.kotlinAndroid) version kotlinVersion apply false
    id(BuildPlugins.navigationSafeArgs) version navigationVersion apply false
    id(BuildPlugins.daggerHiltAndroidPlugin) version hiltVersion apply false
}
