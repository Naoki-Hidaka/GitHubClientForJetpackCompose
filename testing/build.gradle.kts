plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

android {
    applyCommon()
}

dependencies {

    // Hilt
    val hiltVersion = "2.39.1"
    val hiltJetpackVersion = "1.0.0"
    implementation("com.google.dagger:hilt-android:$hiltVersion")

    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    kapt("androidx.hilt:hilt-compiler:$hiltJetpackVersion")

    // Lifecycle
    val lifecycleVersion = "2.3.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    // Coroutine
    val coroutineVersion = "1.5.2"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")

    // Test
    val mockkVersion = "1.12.0"
    implementation("io.mockk:mockk:$mockkVersion")
    implementation("androidx.test.ext:junit-ktx:1.1.3")


    implementation("junit:junit:4.13.2")
    implementation("com.google.truth:truth:1.1.3")
    implementation("androidx.arch.core:core-testing:2.1.0")
    implementation("androidx.test:core-ktx:1.4.0")
    implementation("androidx.test:rules:1.4.0")
    implementation("androidx.test:runner:1.4.0")
}
