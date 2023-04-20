plugins {
    id ("com.android.library")
    id ("org.jetbrains.kotlin.android")
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
}

dependencies {

    implementation (libs.core.ktx)
    api(libs.compose.material3)
    api (libs.bundles.ui)
    implementation (project(":PiAnalytic"))
    api (libs.material.icons.extended)
    api(project(":PiModel"))
    implementation(project(":PiNavigation"))
    debugApi (libs.customview.poolingcontainer)
    // For constraint layout
    api (libs.constraintlayout.compose)

    //For lottie animation integration
    implementation (libs.lottie)
    // To integrate Navigation
    api (libs.accompanist.permissions)

    api (libs.bundles.appcompanist)
    api(libs.coil.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.test.ext)
    androidTestImplementation(libs.espresso.core)
}