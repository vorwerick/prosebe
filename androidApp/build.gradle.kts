plugins {
    id("com.android.application")
    id ("io.sentry.android.gradle") version "3.3.0"
    kotlin("android")
}

android {
    namespace = "cz.dzubera.prosebe.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "cz.dzubera.prosebe.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.1"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui:1.3.1")
    implementation("androidx.compose.ui:ui-tooling:1.3.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.3.1")
    implementation("androidx.compose.foundation:foundation:1.3.1")
    implementation("androidx.compose.material:material:1.3.1")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation ("androidx.navigation:navigation-compose:2.5.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
}