plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    applyCommon()
}

dependencies {

    // Hilt
    val hiltVersion = "2.38"
    val hiltJetpackVersion = "1.0.0"
    implementation("com.google.dagger:hilt-android:$hiltVersion")

    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    kapt("androidx.hilt:hilt-compiler:$hiltJetpackVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")

    // Timber
    implementation("com.jakewharton.timber:timber:4.7.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
}