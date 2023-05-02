buildscript {
    repositories {
        google()
        mavenCentral()

        // Android Build Server
        maven { url = uri("../nowinandroid-prebuilts/m2repository") }
    }
    dependencies {

        classpath("com.google.gms:google-services:${libs.versions.gms.googleServices.get()}")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:${libs.versions.hilt.version.get()}")
        //To pass parameters as part of deeplink
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:${libs.versions.nav.version.get()}")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.version.get()}")
        classpath ("com.google.firebase:firebase-crashlytics-gradle:${libs.versions.firebase.crashlytics.gradle.get()}")
    }
}

// Lists all plugins used throughout the project without applying them.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.kotlin) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.hilt) apply false
}