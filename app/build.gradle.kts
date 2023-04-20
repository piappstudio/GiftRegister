plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
}
// Lists all plugins used throughout the project without applying them.
android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.piappstudio.giftregister"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        debug {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        release {
            isMinifyEnabled = true
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
        kotlinCompilerExtensionVersion = "1.4.0-alpha02"
    }
    packagingOptions {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {

    implementation (libs.core.ktx)
    implementation (libs.compose.ui)
    implementation (libs.compose.material3)
    implementation (libs.activity.compose)
    implementation (libs.lifecycle.runtime.ktx)
    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))
    implementation (libs.firebase.analytics.ktx)
    implementation (libs.firebase.crashlytics.ktx)
    implementation (libs.timber)

    // To integrate Hilt Android
    implementation(project(":welcome"))
    implementation (project(":authentication"))
    implementation (project(":pitheme"))
    implementation (project(":register"))
    implementation(project(":PiCloud"))
    implementation (project(":PiNavigation"))
    implementation (project(":PiNetwork"))
    // To integrate Navigation
    implementation (libs.navigation.compose)


    implementation (libs.retrofit)
    implementation (libs.retrofit.gson)
    implementation (libs.okhttp.logging)

    // To use additional extensions of navigation frameworks like hiltViewModel()
    implementation (libs.hilt.android)
    implementation (project(":PiAnalytic"))

    implementation (libs.hilt.navigation)
    kapt (libs.hilt.android.compiler)
    implementation (libs.hilt.common)
    implementation (libs.hilt.work)
    implementation (libs.work.runtime)
    implementation (libs.nav.fragment.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.test.ext)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ui.test)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

}