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
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles ("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
}

dependencies {

    implementation(libs.core.ktx)

    implementation (libs.bundles.ui)

    implementation (libs.compose.material3)
    // To integrate Navigation
    implementation (project(":pitheme"))
    implementation (project(":PiNavigation"))
    implementation ("androidx.compose.material:material:1.3.0-rc01")


    // Hilt integration

    implementation (libs.bundles.hilt)
    kapt(libs.hilt.android.compiler)

    implementation (libs.paging.common)
    implementation (libs.paging.compose)
    implementation (libs.camera.view)
    implementation ("com.google.ar.sceneform:filament-android:1.17.1")
    implementation (project(":PiAnalytic"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.test.ext)
    androidTestImplementation(libs.espresso.core)
}