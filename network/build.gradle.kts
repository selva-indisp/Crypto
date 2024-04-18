plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlinx-serialization")
}

android {
    namespace = "com.indisp.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":core"))
    implementation(libs.koin)
    implementation(libs.ktx.core)
    implementation(libs.ktor.core)
    implementation(libs.ktor.cio)
    implementation(libs.ktor.contentnegotiation)
    implementation(libs.ktor.jsonserialization)

    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.android)
}