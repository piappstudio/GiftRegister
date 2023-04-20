/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

plugins {
    id ("com.android.library")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-parcelize")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation (libs.core.ktx)
    implementation (libs.compose.material3)
    // Hilt integration
    implementation (libs.bundles.hilt)
    kapt(libs.hilt.android.compiler)
    implementation (libs.paging.common)
    api(libs.coroutines.android)

    // Room components
    implementation(libs.room)
    kapt(libs.room.compiler)
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation (libs.room.ktx)

    api(libs.google.code.gson )
    implementation(libs.crypto)

    // For Timber
    api (libs.timber)

    testImplementation(libs.junit)
    androidTestImplementation(libs.test.ext)
    androidTestImplementation(libs.espresso.core)
}