plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.0"
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.cropswap"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.cropswap"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
        }
    }
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.core.ktx)
    implementation(libs.activity.compose)
    implementation(libs.material3)
    implementation(libs.material.icons.extended)
    implementation(libs.navigation.compose)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.ui.tooling.preview)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.junit.ktx)
    implementation(libs.firebase.database)
    implementation(libs.ui.android)
    implementation(libs.room.common.jvm)
    implementation(libs.room.runtime.android)

}


