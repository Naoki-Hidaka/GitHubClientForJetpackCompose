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

    implementation(project(":data:api"))
    implementation(project(":data:local"))

    // Hilt
    val hiltVersion = "2.38"
    val hiltJetpackVersion = "1.0.0"
    implementation("com.google.dagger:hilt-android:$hiltVersion")

    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    kapt("androidx.hilt:hilt-compiler:$hiltJetpackVersion")

    // Coroutine
    val coroutineVersion = "1.5.0"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")

    // Timber
    implementation("com.jakewharton.timber:timber:4.7.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
}