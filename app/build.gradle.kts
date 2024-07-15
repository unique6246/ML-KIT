plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.ml_kit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ml_kit"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.camera:camera-core:1.3.4")
    implementation ("androidx.camera:camera-camera2:1.3.4")
    implementation ("androidx.camera:camera-view:1.3.4")
    implementation ("com.google.mlkit:object-detection:17.0.1")
    implementation ("org.tensorflow:tensorflow-lite:2.7.0")
    implementation ("androidx.camera:camera-lifecycle:1.3.4")
    implementation ("org.tensorflow:tensorflow-lite-support:0.2.0")
    implementation ("org.tensorflow:tensorflow-lite:2.7.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}