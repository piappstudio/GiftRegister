/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

plugins {
    id ("com.android.library")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles ("consumer-rules.pro")
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.version.get()
    }
    packagingOptions {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {

    implementation (libs.core.ktx)
    implementation (libs.bundles.ui)
    implementation (libs.compose.material3)
    implementation (libs.activity.compose)
    implementation (libs.navigation.compose)


    implementation (libs.bundles.hilt)
    implementation (libs.hilt.work)
    kapt(libs.hilt.android.compiler)

    implementation (project(":pitheme"))
    implementation (project(":PiModel"))
    implementation (project(":PiNavigation"))

    implementation (libs.work.runtime)

    // Google drive authentication
    implementation (libs.gms.play.services.auth)
    implementation (libs.gms.play.services.drive)
    // Google drive integration
    implementation (libs.google.api.services.drive)
    implementation (libs.api.client.android)
    implementation (libs.oauth2.http)

    testImplementation(libs.junit)
    androidTestImplementation(libs.test.ext)
    androidTestImplementation(libs.espresso.core)
}